package com.shopme.admin.product;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{

	public Product findByName(String name);
}
