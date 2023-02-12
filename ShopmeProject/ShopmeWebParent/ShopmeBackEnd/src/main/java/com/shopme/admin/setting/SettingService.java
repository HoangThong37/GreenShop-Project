package com.shopme.admin.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;

@Service
public class SettingService {
    
	 @Autowired
	 private SettingRepository repoSetting;
	 
	 
	 public List<Setting> listAllSettings() {
		 return (List<Setting>) repoSetting.findAll();
	 }
	
}
