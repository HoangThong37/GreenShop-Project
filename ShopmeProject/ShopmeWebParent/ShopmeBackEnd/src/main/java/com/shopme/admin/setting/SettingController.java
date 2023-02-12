package com.shopme.admin.setting;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

@Controller
public class SettingController {
     
	@Autowired
	private SettingService service;
	
	@Autowired
	private CurrencyRepository currencyRepo;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
	    List<Setting> listSettings = service.listAllSettings();  // 
	    List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc(); // ds tiền tệ
	       
		model.addAttribute("listCurrencies", listCurrencies);
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		return "/settings/settings";
	}
}
