package com.shopme.admin.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.repository.CurrencyRepository;
import com.shopme.admin.service.SettingService;
import com.shopme.admin.util.FileUploadUntil;
import com.shopme.admin.util.GenaralSettingBag;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Setting;

@Controller
public class SettingController {

	@Autowired
	private SettingService service;

	@Autowired
	private CurrencyRepository currencyRepo;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = service.listAllSettings(); //
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc(); // ds tiền tệ

		model.addAttribute("listCurrencies", listCurrencies);
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		return "/settings/settings";
	}

	// saveGenaralSettings
	// saveCurrencySymbol
	// saveSiteLogo
	// updateSettingValuesFromForm
	@PostMapping("/settings/save_general")
	public String save(@RequestParam("fileImage") MultipartFile multipartFile, HttpServletRequest request,
			RedirectAttributes ra) throws IOException {
		GenaralSettingBag settingBag = service.getGeneralSetting(); // save general
		saveSiteLogo(multipartFile, settingBag); // lưu logo
		saveCurrencySymbol(request, settingBag); // lưu CURRENCY_SYMBOL
		
		updateSettingValuesFromForm(request, settingBag.list()); // update cài đặt giá trị từ form

		ra.addFlashAttribute("messageSuccess", "The setting has been saved successfully.");
		return "redirect:/settings";
	}

	private void saveSiteLogo(MultipartFile multipartFile, GenaralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;

			settingBag.updateSiteLogo(value);

			String uploadDir = "../site-logo/";
			FileUploadUntil.cleanDir(uploadDir);
			FileUploadUntil.saveFile(uploadDir, fileName, multipartFile);

		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, GenaralSettingBag settingBag) { // lưu kí hiệu tiền tệ
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult =  currencyRepo.findById(currencyId); // get Id
		
		if (findByIdResult.isPresent() ) { // isPresent: value != null
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}
	
	 private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		 for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		 service.saveAll(listSettings);
	 }
	 
	 // save mail_server setting
	 @PostMapping("/settings/save_mail_server")
	 public String saveMailServerSetting(HttpServletRequest request, RedirectAttributes ra) {
		 List<Setting> mailServerSettings   = service.getMailServerSetting();
		 updateSettingValuesFromForm(request, mailServerSettings); // save 
		 
		 ra.addFlashAttribute("messageSuccess", "Mail server settings have been saved");  // message success
		 return "redirect:/settings#mailServer"; // return về String
	 }
	 
	 // save mail_server setting
	 @PostMapping("/settings/save_mail_templates")
	 public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes ra) {
		 List<Setting> mailTemplateSettings   = service.getMailTemplateSetting();
		 updateSettingValuesFromForm(request, mailTemplateSettings); // save 
		 
		 ra.addFlashAttribute("messageSuccess", "Mail template settings have been saved");  // message success
		 return "redirect:/settings"; // return về String
	 }
}
