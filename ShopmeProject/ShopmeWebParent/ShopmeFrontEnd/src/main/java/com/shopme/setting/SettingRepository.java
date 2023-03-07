package com.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	  // repo
	  List<Setting> findByCategory(SettingCategory category);
	  
	  @Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
	  List<Setting> findByTwoCategories(SettingCategory cat1, SettingCategory cat2);

}
