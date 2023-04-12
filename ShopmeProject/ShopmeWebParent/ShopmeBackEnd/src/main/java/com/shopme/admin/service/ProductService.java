package com.shopme.admin.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.ProductRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductService {
	
	public static final int PRODUCTS_PER_PAGE = 5;

	@Autowired
	private ProductRepository repo;
	

	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}
	
	// page
	public void listByPage(int number, PagingAndSortingHelper helper, Integer categoryId) {
		Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, number);
		String keyword = helper.getKeyword();
		Page<Product> page = null;

		// search keyword
		if (keyword != null && !keyword.isEmpty()) { 
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.searchInCategory(categoryId, categoryIdMatch,keyword , pageable);
			} else {
				page = repo.findAll(keyword, pageable);
			}
		} else {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
			} else {
				page = repo.findAll(pageable);
			}
		}
		helper.updateAttributes(number, page);
	}

	public Product save(Product product) {
		if (product.getId() != null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getAlias().replaceAll("", "-"); // đặt bí danh mặc định nếu chưa đc chỉ định
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		product.setUpdatedTime(new Date());

		return repo.save(product);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = repo.findByName(name);

		if (isCreatingNew) {
			if (productByName != null)
				return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}

	public void updateEnabled(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws ProductNotFoundException {
		Long idDeleted = repo.countById(id);
		if (idDeleted == null || idDeleted == 0) {
			throw new ProductNotFoundException("Could not find any product with ID : " + id);
		}
		repo.deleteById(id);

	}
	public Product get(Integer id) throws ProductNotFoundException{
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any product with ID : " + id);
		}
	}
	
	// save product Price [role saleperson]
	public void saveProductPrice(Product productInForm) {
		Product product = repo.findById(productInForm.getId()).get(); // lấy product cần save
		product.setCost(productInForm.getCost());
		product.setPrice(productInForm.getPrice());
		product.setDiscountPercent(productInForm.getDiscountPercent());
		
		repo.save(product);
	}
	
}
