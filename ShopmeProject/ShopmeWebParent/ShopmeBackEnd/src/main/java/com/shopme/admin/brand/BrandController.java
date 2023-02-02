package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

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

import com.shopme.admin.FileUploadUntil;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Controller
public class BrandController {
	
	//private String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc";

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
//	@GetMapping("/brands")
//	public String listFirstPage() {
//		return defaultRedirectURL;
//	}
//	
//	@GetMapping("/brands/page/{pageNum}")
//	public String listByPage(
//			@PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper,
//			@PathVariable(name = "pageNum") int pageNum
//			) {
//		brandService.listByPage(pageNum, helper);
//		return "brands/brands";		
//	}
	
	@GetMapping("/brands/page/{pageNumber}")
	public String listPage(@PathVariable(name = "pageNumber") int page ,Model model, 
			               @Param("sortField") String sortField,
			               @Param("sortDir") String sortDir,
			               @Param("keyword") String keyword) {
		
		Page<Brand> pageBrand =brandService.listByPage(page, sortField, sortDir, keyword);
		List<Brand> listBrand = pageBrand.getContent();
		
		long startCount = (page - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		if (endCount > pageBrand.getTotalElements()) {
			endCount = pageBrand.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", pageBrand.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageBrand.getTotalElements());
		model.addAttribute("listBrand", listBrand);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("moduleURL", "/brands");
		
		return "/brands/brands";
	}
	
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
