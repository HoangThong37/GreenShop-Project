package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.exception.ProductNotFoundException;
import com.shopme.admin.helper.ProductSaveHelper;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.service.ProductService;
import com.shopme.admin.util.FileUploadUntil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {

	@Autowired
	private ProductService serviceProduct;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;	
	
//	@GetMapping("/products")
//	public String listAll(Model model) {
//		List<Product> listProducts = serviceProduct.listAll(); 
//		
//		model.addAttribute("listProducts", listProducts);
//		return "products/products";
//	}
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null, 0);
	}
	
	// product : sản phẩm
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, 
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId

			) {
		//System.out.println("Seleted category Id : " + categoryId);
		List<Category> listCategories = categoryService.listCategoriesUsedInForm(); // list --
		
		Page<Product> page = serviceProduct.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> listProducts = page.getContent();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        
		if (categoryId != null) model.addAttribute("categoryId", categoryId);
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);		
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		

		return "products/products";		
	}
	
	
	//Brand : nhãn hiệu
	@GetMapping("/products/new") 
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setInStock(true);
		product.setEnabled(true);
		
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("product",product);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0); // extra image
		
		return "products/products_form";
	}
	
	@PostMapping("/products/save") 
	public String saveProduct(Product product,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultiparts,  // image main
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts, // image extra
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,  // id ảnh
			@RequestParam(name = "imageNames", required = false) String[] imageNames, // 
			@AuthenticationPrincipal ShopmeUserDetails loggedUser, // authen 
			RedirectAttributes ra) throws IOException {
		
		    if (loggedUser.hasRole("Salesperson")) { // với role Saleperson -> save
		    	serviceProduct.saveProductPrice(product);
				ra.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
				return "redirect:/products";
			}
		   
		  //  ProductSaveHelper.setMainImageName(mainImageMultiparts,product);
		    ProductSaveHelper.setExistingExtraImageName(imageIDs, imageNames, product); // set tên hình ảnh
		    ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		    ProductSaveHelper.setProductDetails(detailIDs,detailNames, detailValues, product);
	
			Product savedProduct = serviceProduct.save(product);
			ProductSaveHelper.saveUploadedImages(mainImageMultiparts, extraImageMultiparts, savedProduct);
			
			ProductSaveHelper.deleteExtraImagesWeredRemoveOnForm(product);
			
		ra.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
		return "redirect:/products";
	} 
	
	
	
	// code update product enabled
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
			                                 @PathVariable("status") boolean enabled,
			                                 RedirectAttributes redirectAttributes) {
		serviceProduct.updateEnabled(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("messageSuccess", message);

		return "redirect:/products";
	}
	
	// code delete product
	@GetMapping("/products/delete/{id}")
	public String deleteProducts(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			serviceProduct.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productMainImage = "../product-images/" + id;
			FileUploadUntil.removeDir(productExtraImagesDir);
			FileUploadUntil.removeDir(productMainImage);
			
		} catch (ProductNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
		}
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {

		try {
			Product product = serviceProduct.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();


			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/products_form";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("messageError", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {

		try {
			Product product = serviceProduct.get(id);

			model.addAttribute("product", product);

			return "products/products_detail_modal";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("messageError", e.getMessage());
			return "redirect:/products";
		}
	}
}
