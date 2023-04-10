package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.shopme.admin.exception.BrandNotFoundException;
import com.shopme.admin.export.csv.BrandCsvExporter;
import com.shopme.admin.export.execl.BrandExcelExporter;
import com.shopme.admin.export.pdf.BrandPdfExporter;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.util.FileUploadUntil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
	
	private BrandService brandService;
	
	private CategoryService categoryService;
	
	@Autowired
	public BrandController(BrandService brandService, CategoryService categoryService) {
		super();
		this.brandService = brandService;
		this.categoryService = categoryService;
	}

	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
		//return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		
		return "brands/brands_form";		
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());	
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();

			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			brandService.save(brand);
		}

		ra.addFlashAttribute("messageSuccess", "The brand has been saved successfully.");
		return "redirect:/brands";		
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {

		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			

			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

			return "brands/brands_form";			
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {

		try {
			brandService.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUntil.removeDir(brandDir);	
			redirectAttributes.addFlashAttribute("messageSuccess", 
					"The brand ID " + id + " has been deleted successfully");
		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
		}
		return "redirect:/brands";
	}	
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		brandService.listByPage(pageNum, helper);

		return "brands/brands";		
	}
	
	// code export csv list brand
	@GetMapping("/brands/export/csv")
	public void exportCategoryByCsv(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listAll();
		BrandCsvExporter brandCsvExporter = new BrandCsvExporter();
		brandCsvExporter.export(listBrands, response);
	}

	// code export export list brand
	@GetMapping("/brands/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listAll();
		BrandExcelExporter exporter = new BrandExcelExporter();
		exporter.export(listBrands, response);
	}
	
	// code export export list brand
	@GetMapping("/brands/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listAll();
		BrandPdfExporter exporter = new BrandPdfExporter();
		exporter.export(listBrands, response);
	}
}