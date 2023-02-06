package com.shopme.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.brand.BrandService;
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
	
	
}
