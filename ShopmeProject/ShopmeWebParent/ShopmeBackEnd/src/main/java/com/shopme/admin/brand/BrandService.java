package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
@Transactional
public class BrandService {
	
	@Autowired
	private BrandRepository repo;
	
	public List<Brand> listAll() {
		return (List<Brand>) repo.findAll();
	}
	
	public Brand save(Brand brand) {
		return repo.save(brand);
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
