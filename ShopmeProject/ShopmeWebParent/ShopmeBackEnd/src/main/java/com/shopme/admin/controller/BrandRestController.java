package com.shopme.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.dto.CategoryDTO;
import com.shopme.admin.exception.BrandNotFoundException;
import com.shopme.admin.exception.BrandNotFoundRestException;
import com.shopme.admin.service.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@RestController
public class BrandRestController {
	
	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name){
	    return brandService.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id") Integer brandId) throws BrandNotFoundRestException {
	   List<CategoryDTO> listCategory = new ArrayList<>();
		
		try {
			Brand brand  =	brandService.get(brandId); // lấy id nhãn hàng
		    Set<Category> categories =	brand.getCategories();  // lấy danh mục với nhãn hàng ấy
		    
		    for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategory.add(dto);
			}
		    return listCategory;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
}
