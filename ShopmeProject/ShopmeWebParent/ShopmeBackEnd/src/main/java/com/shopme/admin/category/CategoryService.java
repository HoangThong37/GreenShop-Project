package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Category;

@Service
//@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public List<Category> listAll() {
		List<Category> rootCategories = repository.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for(Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			
		   Set<Category> children =  rootCategory.getChildren();
		   for(Category item : children) {
			   String name = "--" + item.getName();
			   hierarchicalCategories.add(Category.copyFull(item, name));
			   listHierarchicalCategories(hierarchicalCategories, item, 1);
		   }
		}
		return hierarchicalCategories;
	}
	
	private void listHierarchicalCategories(List<Category> hierarchicalCategories,
			Category parent, int subLevel) {
		
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listHierarchicalCategories(hierarchicalCategories ,subCategory, newSubLevel);
		}
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
					listSubCategoriesUsedInForm(result, item, 1);
				}
			}
		}
		return result;
	}

	private void listSubCategoriesUsedInForm(List<Category> result, Category parent, int subLevel) {
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
			listSubCategoriesUsedInForm(result ,subCategory, newSubLevel);
		}
	}

	public Category get(Integer id) throws CategoryNotFoundException {
		//return repository.findById(id).get();
		try {
			return repository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find any category with ID : " + id);
		}
	}
}
