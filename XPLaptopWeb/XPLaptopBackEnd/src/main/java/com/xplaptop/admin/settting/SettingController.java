package com.xplaptop.admin.settting;

import java.io.IOException;
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

import com.xplaptop.admin.FileUploadUtils;
import com.xplaptop.common.entity.setting.Currency;
import com.xplaptop.common.entity.setting.GeneralSettingBag;
import com.xplaptop.common.entity.setting.Setting;

@Controller
public class SettingController {
	
	@Autowired
	private SettingService service;
	
	@Autowired CurrencyRepository currencyRepository;
	
	@GetMapping("/settings")
	public String settingsPage(Model model) {
		List<Setting> allSettings = service.getAllSettings();
		for(Setting setting : allSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		
		List<Currency> allCurrencies = service.getAllCurrencies();
		model.addAttribute("allCurrencies", allCurrencies);
		
		model.addAttribute("title", "Setting");
		return "settings/settings";
	}
	
	@PostMapping("setting/save_general") 
	public String saveGeneralSetting(
			@RequestParam(name = "siteLogo") MultipartFile multipartFile,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException {
		GeneralSettingBag generalSettingBag = service.getGeneralSettingBag();
		
		saveSiteLogo(multipartFile, generalSettingBag);
		saveCurrencySymbol(request, generalSettingBag);
		updateSettingValuesFromForm(request, generalSettingBag.list());
		
		redirectAttributes.addFlashAttribute("message", "General Setting has been saved successfully");
		
		return "redirect:/settings";
	}
	
	@PostMapping("/setting/save_mail_server")
	public String saveMailServerSetting(
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		List<Setting> mailServerSetting = service.getMailServerSetting();
		updateSettingValuesFromForm(request, mailServerSetting);
		
		redirectAttributes.addFlashAttribute("message", "Mail Server Setting has been saved successfully");
		return "redirect:/settings#mailserver";
	}
	
	@PostMapping("/setting/save_mail_tamplates")
	public String saveMailTemplatesSetting(
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		List<Setting> mailTemplatesSetting = service.getMailTemplatesSetting();
		updateSettingValuesFromForm(request, mailTemplatesSetting);
		
		redirectAttributes.addFlashAttribute("message", "Mail Templates Setting has been saved successfully");
		return "redirect:/settings#mailtemplates";
	}
	
	

	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag generalSettingBag) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/"+fileName;
			
			generalSettingBag.updateSiteLogo(value);
			
			String uploadDir = "../site-logo/";
			FileUploadUtils.cleanDir(uploadDir);
			FileUploadUtils.uploadFile(uploadDir, fileName, multipartFile);
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag generalSettingBag) {
		Integer currencyId= Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> currencyFindById = currencyRepository.findById(currencyId);
		
		if(currencyFindById.isPresent()) {
			Currency currency = currencyFindById.get();
			String symbol = currency.getSymbol();
			generalSettingBag.updateCurrencySymbol(symbol);
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for(Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if(value != null) {
				setting.setValue(value);
			}
		}
		service.saveAll(listSettings);
	}
}
