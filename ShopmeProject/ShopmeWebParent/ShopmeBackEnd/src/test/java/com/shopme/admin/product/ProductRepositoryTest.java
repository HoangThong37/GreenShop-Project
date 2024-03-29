package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.repository.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 5);
		Category category = entityManager.find(Category.class, 6);
		
		Product product = new Product();
		product.setName("test Abc Desktop");
		product.setAlias("test");
		product.setShortDescription("Short description for Acer Aspire");
		product.setFullDescription("Full description for Acer Aspire");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(450);
		product.setCost(400);
		product.setEnabled(true);
		product.setInStock(true);
		
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProducts() {
	  Iterable<Product> list  =	 repo.findAll();

	  list.forEach(System.out::println);
	}
	
	@Test
	public void testGetProducts() {
	 Product idProduct = repo.findById(2).get();
	 System.out.println(idProduct);
	 assertThat(idProduct).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer idProduct = 2;
		Product product = repo.findById(idProduct).get();
			product.setCost(400);
			product.setInStock(true);
	  Product productSave	= repo.save(product);
	 // assertThat(productSave).isNotNull();

	}
	
	@Test
	public void test2DeleteProduct() {
		Integer idProduct = 5;
		repo.deleteById(idProduct);
		
		Optional<Product> product =repo.findById(4);
		System.out.println(product + "coi có ko");
	 // assertThat(productSave).isNotNull();

	}
	
	@Test
	public void testSaveProductWithImage() {
		Integer idProduct = 1;
		Product product = repo.findById(idProduct).get();
		
		product.setMainImage("main_Image.png");
		product.addExtraImage("extra_image1.png");
		product.addExtraImage("extra_image2.png");
		product.addExtraImage("extra_image3.png");
	
	    Product saveImageProduct = repo.save(product);
	    assertThat(saveImageProduct.getImages().size()).isEqualTo(3);
	}
	
	@Test
	public void testSaveProductWithDetails() {
		Integer idProduct = 1;
		Product product = repo.findById(idProduct).get();
		
		product.addDetail("Device Memory", "128 GB");
		product.addDetail("CPU Model", "MediaTek");
		product.addDetail("OS", "Adroid 10");
		
	    Product saveProduct = repo.save(product);
	    assertThat(saveProduct.getDetails()).isNotEmpty();
	}
}
