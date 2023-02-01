package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUntil;
import com.shopme.admin.category.CategoryCsvExporter;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listAll(Model model) {
		List<Brand> listBrands = brandService.listAll();
		model.addAttribute("listBrands", listBrands);
		return "brands/brands";
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model){
	    List<Category> listCategories =categoryService.listCategoriesUsedInForm();
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Tạo Thương Hiệu");
		model.addAttribute("brand", new Brand());
		return "brands/brands_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand saveBrands = brandService.save(brand);
			String uploadDir = "../brand-logos/" + saveBrands.getId();
			
			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			brandService.save(brand);
		}
		ra.addFlashAttribute("message", "The brand has been saved successfully");
		return "redirect:/brands";
	}
	
	
	// code update brands
		@GetMapping("/brands/edit/{id}")
		public String editBrand(@PathVariable(name = "id") Integer id, Model model,
				RedirectAttributes redirectAttributes) {
			try {
				Brand brand = brandService.get(id); // lấy id cần edit
				List<Category> listCategories = categoryService.listCategoriesUsedInForm(); // edit list category

				model.addAttribute("brand", brand);
				model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
				model.addAttribute("listCategories", listCategories);

				return "brands/brands_form";
			} catch (BrandNotFoundException ex) {
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
				return "redirect:/brands";
			}
		}
		
		
		// code delete brands
		@GetMapping("/brands/delete/{id}")
		public String deleteCategory(@PathVariable(name = "id") Integer id,
				Model model,
				RedirectAttributes redirectAttributes) { //chuyển tiếp trang
			try {
				brandService.delete(id);
				String brandDir = "../brand-logos/" + id; // delete image
				FileUploadUntil.removeDir(brandDir);
				
				redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " deleted successfully");
			} catch (BrandNotFoundException ex) {
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
			}
			return "redirect:/brands";
		}
		

	
}
