package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Product;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository repo;

	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
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
		repo.updateEnabledAndStatus(id, enabled);
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
	
}
