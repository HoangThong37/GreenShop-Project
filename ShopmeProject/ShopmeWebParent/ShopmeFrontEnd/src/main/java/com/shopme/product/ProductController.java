package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
    
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) throws CategoryNotFoundException {
		return viewCategory(alias, 1, model);
	}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}") 
	public String viewCategory(@PathVariable("category_alias") String alias,
		                       @PathVariable("pageNum") Integer pageNum, 
			                   Model model) throws CategoryNotFoundException {
		Category category = categoryService.getCategory(alias); // get category theo alias
		if (category == null) {
			return "error/404";
		}
		
		List<Category> listCategoriesParent  = categoryService.getCategoryParents(category);
		
		Page<Product> page = productService.listByCategory(pageNum, category.getId());
		List<Product> listProducts = page.getContent();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCategoriesParent", listCategoriesParent);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("category", category);
		
		return "products_by_category";
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
		try {
		 Product product = productService.getProduct(alias);
		 List<Category> listCategoriesParent  = categoryService.getCategoryParents(product.getCategory()); //
		 
		 model.addAttribute("product", product);
		 model.addAttribute("listCategoriesParent", listCategoriesParent);
		 
		 return "product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
}


