package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.User;


@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

	public Long countById(Integer id); // count id -> method delete
	
	public Brand findByName(String name); // lấy id theo name
	
	//seach 
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> search(String keyword, Pageable pageable);
	
	//asc
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();
}
