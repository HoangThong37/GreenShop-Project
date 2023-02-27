package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {
   
	@Autowired
	private CategoryRepository repo;
	
	public List<Category> listNoChildrenCategories() {
		List<Category> listNoChildrenCategories = new ArrayList<>();
		List<Category> listCategoriesEnabled = repo.findAllEnabled(); // list d/s categories
		
		listCategoriesEnabled.forEach(category -> {
			   Set<Category> children = category.getChildren(); 
			   if (children == null || children.size() == 0) {
				   listNoChildrenCategories.add(category);
			}
		});
		return listNoChildrenCategories;
	}
	
	// get thể loại
	public Category getCategory(String alias) throws CategoryNotFoundException {
	    Category category = repo.findByAliasEnabled(alias);
	    if (category == null) {
			throw new CategoryNotFoundException("Could not find any categories with alias" + alias);
		}
	    return category;
	}
	
	// get thể loại cha
	public List<Category> getCategoryParents(Category child) {
		List<Category> listParents = new ArrayList<>(); // 1 list rỗng
		Category parent = child.getParent(); 
		
		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent(); // lấy tiếp parent nếu có
		}
		listParents.add(child);
		return listParents;
	}
}
