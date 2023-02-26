package com.shopme.admin.service;

import java.util.ArrayList;
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
import com.shopme.admin.exception.BrandNotFoundException;
import com.shopme.admin.repository.BrandRepository;

@Service
//@Transactional
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 10;
	
	@Autowired
	private BrandRepository repo;
	
	public List<Brand> listAll() {
		
		Sort firstNameSorting =  Sort.by("name").ascending();
		
		List<Brand> brandList = new ArrayList<>();
		repo.findAll(firstNameSorting).forEach(brandList::add);
		return brandList;
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
			return repo.search(keyword, pageable);
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
		// TODO Auto-generated method stub
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = repo.findByName(name);

		if (isCreatingNew) {
			if (brandByName != null) return "Duplicate";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
	
	

}
