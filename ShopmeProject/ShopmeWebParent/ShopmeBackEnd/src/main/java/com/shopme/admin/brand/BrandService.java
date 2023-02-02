package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.User;

@Service
@Transactional
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 5;
	
	@Autowired
	private BrandRepository repo;
	
	public List<Brand> listAll() {
		return (List<Brand>) repo.findAll();
	}
	
	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	
	public Page<Brand> listByPage(int number, String sortField, String sortDir, String keyword) {
		// sortDir : asc or desc
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(number - 1, BRANDS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		return repo.findAll(pageable);
	}
	
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);
		if(id == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
		repo.deleteById(id);
	} 
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand with ID : " + id);
		}
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == 0 || id == null); // chưa có brand id
		Brand brandName = repo.findByName(name); // lấy name brand
		
		if (isCreatingNew) {
			 if (brandName != null) {
				return "Duplicate";
			}
		} else { // có id rồi
			if (brandName != null && brandName.getId() != id ) {
				return "Duplicate";
			}
		}
		return "OK";
	}
	
	
	

}
