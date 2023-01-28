package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.lowagie.text.pdf.AcroFields.Item;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository repository;
	
	@Test
	public void testCreateRepository() {
		Category category = new Category("Electronics");
		Category saveCategory = repository.save(category);
		
		assertThat(saveCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(9);
		Category cam = new Category("Gaming Laptops", parent);
		//Category smartphone = new Category("Smartphone", parent);
	    //repository.saveAll(List.of(cam, smartphone));
		 Category saveCategory = repository.save(cam);
		 assertThat(saveCategory.getId()).isGreaterThan(0);
	}
	
	// get category
	@Test
	public void testgetCategory() {
		Category getCategory = repository.findById(1).get();
		System.out.println("Tên thể loại : " + getCategory.getName());
		
		Set<Category> list = getCategory.getChildren();
		for(Category itemCategory : list) {
			System.out.println(itemCategory.getName());
		}
		 assertThat(list.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintCategory() {
	  Iterable<Category> categorys = repository.findAll(); // get all
	  for(Category category : categorys) {   // duyệt all
		  if (category.getParent() == null) {
			System.out.println(category.getName()); // in parent
			
			Set<Category> children = category.getChildren();
			for(Category item : children) {
				System.out.println("-- " + item.getName());
			}
		}
	  }
	}
	
	@Test
	public void testMethodListRootCategories() {
		List<Category> categories = repository.findRootCategories(Sort.by("name").ascending());
		categories.forEach(item -> System.out.println(item.getName()));
	}

}
