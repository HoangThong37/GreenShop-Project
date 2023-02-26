package com.shopme.admin.util;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

public class GenaralSettingBag extends SettingBag {

	public GenaralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
    
	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}
	
	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}
}
