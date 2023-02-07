package com.shopme.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUntil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
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
	public String saveProduct(Product product, RedirectAttributes ra) {
		
	    serviceProduct.save(product);
	    ra.addFlashAttribute("message", "The product has been saved successfully.");
		
//		System.out.println("product name : " + product.getName());
//		System.out.println("brand id : " + product.getBrand().getId());
//		System.out.println("Category id : " + product.getCategory().getId());
		
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
		} catch (ProductNotFoundException ex) {
			ra.addFlashAttribute("messageError", ex.getMessage());
		}
		return "redirect:/products";
	}
}
