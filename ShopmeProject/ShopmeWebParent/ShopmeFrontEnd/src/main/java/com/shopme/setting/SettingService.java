package com.shopme.setting;

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

	// saveAll
	// getGeneralSetting
	public List<Setting> getGeneralSetting() {

//		List<Setting> settings = new ArrayList<>();
//		List<Setting> genaralSettings = repoSetting.findByCategory(SettingCategory.GENERAL);
//		List<Setting> currencySettings = repoSetting.findByCategory(SettingCategory.CURRENCY);
//		settings.addAll(genaralSettings);
//		settings.addAll(currencySettings);
//		return settings;
		return repoSetting.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = repoSetting.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repoSetting.findByCategory(SettingCategory.MAIL_TEMPLATES));
		
		return new EmailSettingBag(settings);
	}
}
