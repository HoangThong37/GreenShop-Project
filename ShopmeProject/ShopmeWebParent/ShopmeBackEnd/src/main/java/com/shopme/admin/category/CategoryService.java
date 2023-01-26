package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
//@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public List<Category> listAll() {
		return (List<Category>) repository.findAll();
	}
	
	public Category save(Category category) {
		return repository.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> result = new ArrayList<>();

		Iterable<Category> categoryInDB = repository.findAll(); // get all
		for (Category category : categoryInDB) { // duyệt all
			if (category.getParent() == null) {
				result.add(Category.copyIdAndName(category));
				// System.out.println(category.getName()); // in parent

				Set<Category> children = category.getChildren();
				for (Category item : children) {
					String name = "--" + item.getName();
					result.add(Category.copyIdAndName(item.getId(), name));
					printChildren(result, item, 1);
				}
			}
		}
		return result;
	}

	private void printChildren(List<Category> result, Category parent, int subLevel) {
		// TODO Auto-generated method stub
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			
			result.add(Category.copyIdAndName(subCategory.getId(), name));
			printChildren(result ,subCategory, newSubLevel);
		}
	}
}
