package com.shopme.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repo;
	
	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}
	
	public Product save(Product product) {
		if (product.getId() != null ) {
			product.setCreatedTime(new Date());
		}
		
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getAlias().replaceAll("" , "-"); // đặt bí danh mặc định nếu chưa đc chỉ định
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		product.setUpdatedTime(new Date());
		
		return repo.save(product);
	}
	
//	public String checkUniqueness(Integer id,String name) {
//		boolean isCreating = (id == 0 || id == null);
//		Product product = repo.findByName(name);
//		
//		if(isCreating) { // tạo mới
//			if (product != null) { //sản phẩm có r 
//				return "Duplicate";
//			}
//		} else { // 
//			if (product != null && product.getId() != id) { //sản phẩm có r 
//				return "Duplicate";
//			}
//		}
//		return "OK";
//	}
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = repo.findByName(name);

		if (isCreatingNew) {
			if (productByName != null) return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}

}
