package com.shopme.admin.product;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.repository.query.Param;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class ProductRestController {
//	
//	@Autowired
//	private ProductService productService;
//	
//	@PostMapping("/products/check_unique")
//	public String checkUniqueness(@Param("id") Integer id, @Param("name") String name) {
//		// productService.checkUniqueness(id, name);
//		return productService.checkUnique(id, name);
//	}
// 
//}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shopme.admin.product.ProductService;

@RestController
public class ProductRestController {

	@Autowired 
	private ProductService service;

	@PostMapping("/products/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return service.checkUnique(id, name);
	}	
}