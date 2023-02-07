package com.shopme.admin.product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{

	public Product findByName(String name);
	
	@Query("UPDATE Product p set p.enabled = ?2 where p.id = ?1")
	@Modifying // cập nhật dữ liệu db
	public void updateEnabledAndStatus(Integer id, boolean enabled);
	
	public Long countById(Integer id); //  method delete
}
