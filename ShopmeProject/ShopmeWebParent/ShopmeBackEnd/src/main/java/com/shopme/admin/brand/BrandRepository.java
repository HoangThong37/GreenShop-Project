package com.shopme.admin.brand;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Brand;


@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

	
	public Long countById(Integer id); // count id -> method delete
	
	public Brand findByName(String name); // lấy id theo name
	
}
