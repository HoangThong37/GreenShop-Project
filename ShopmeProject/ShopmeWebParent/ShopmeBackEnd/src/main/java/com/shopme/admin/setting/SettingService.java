package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {
    
	 @Autowired
	 private SettingRepository repoSetting;
	 
	// getAllSettings
	 public List<Setting> listAllSettings() {
		 return (List<Setting>) repoSetting.findAll();
	 }
	
	 // saveAll
	 // getGeneralSetting
	 public GenaralSettingBag getGeneralSetting() {
		 List<Setting> settings = new ArrayList<>();
		 
		 List<Setting> genaralSettings  = repoSetting.findByCategory(SettingCategory.GENERAL);
		 List<Setting> currencySettings  = repoSetting.findByCategory(SettingCategory.CURRENCY);
		 
		 settings.addAll(genaralSettings);
		 settings.addAll(currencySettings);
		return new GenaralSettingBag(settings);
	 }
	 
	 public void saveAll(Iterable<Setting> setting) {
		  repoSetting.saveAll(setting);
	 }
}