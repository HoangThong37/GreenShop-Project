package com.shopme.admin.product;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.List;

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
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService serviceProduct;
	
	@Autowired
	private BrandService brandService;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = serviceProduct.listAll(); 
		
		model.addAttribute("listProducts", listProducts);
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
		
		return "products/products_form";
	}
	
	@PostMapping("/products/save") 
	public String saveProduct(Product product,
			@RequestParam("fileImage") MultipartFile mainImageMultiparts,  // image main
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts, // image extra
			RedirectAttributes ra) throws IOException {
		
		    setMainImageName(mainImageMultiparts,product);
		    setExtraImageNames(extraImageMultiparts, product);
	
			Product savedProduct = serviceProduct.save(product);
			
			saveUploadedImages(mainImageMultiparts, extraImageMultiparts, savedProduct);
			
		ra.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
		return "redirect:/products";
	} 
	
	private void saveUploadedImages(MultipartFile mainImageMultiparts,
			                        MultipartFile[] extraImageMultiparts,
			                        Product savedProduct) throws IOException {
		if (!mainImageMultiparts.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultiparts.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, mainImageMultiparts);
		}
		if (extraImageMultiparts.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if(multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);
			}			
		} 
	}
	
	//set image in extra
	private void setExtraImageNames(MultipartFile[] extraImageMultiparts,Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				product.addExtraImages(fileName);
			}
			}
		}
	}
	
	//set image in main
	private void setMainImageName(MultipartFile mainImageMultiparts, Product product) {
		if (!mainImageMultiparts.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultiparts.getOriginalFilename());

			product.setMainImage(fileName);
		}
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
}
