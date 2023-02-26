package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.exception.CategoryNotFoundException;
import com.shopme.admin.export.csv.CategoryCsvExporter;
import com.shopme.admin.export.execl.CategoryExcelExporter;
import com.shopme.admin.export.pdf.CategoryPdfExporter;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.util.CategoryPageInfo;
import com.shopme.admin.util.FileUploadUntil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, Model model) {

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo pageInfo = new CategoryPageInfo();

		List<Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDir, keyword);

		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);

		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);

		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model) {

		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Category");

		return "categories/category_form";

	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			category.setImage(fileName);

			Category savedCategory = categoryService.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();

			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			categoryService.save(category);
		}

		ra.addFlashAttribute("messageSuccess", "The category has been saved successfully.");
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {

		try {
			Category category = categoryService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();

			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

			return "categories/category_form";
		} catch (CategoryNotFoundException ex) {

			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {

		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status;

		redirectAttributes.addFlashAttribute("messageSuccess", message);

		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			categoryService.delete(id);

			String categoryDir = "../category-images/" + id;

			FileUploadUntil.removeDir(categoryDir);

			redirectAttributes.addFlashAttribute("messageSuccess",
					"The category ID " + id + " has been deleted successfully");

		} catch (CategoryNotFoundException ex) {

			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}

		return "redirect:/categories";
	}

	// code export csv list category
	@GetMapping("/categories/export/csv")
	public void exportCategoryByCsv(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		CategoryCsvExporter category = new CategoryCsvExporter();
		category.export(listCategories, response);
	}

	// code export export list category
	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		CategoryExcelExporter exporter = new CategoryExcelExporter();
		exporter.export(listCategories, response);
	}
	
	// code export export list category
	@GetMapping("/categories/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		CategoryPdfExporter exporter = new CategoryPdfExporter();
		exporter.export(listCategories, response);
	}
}
