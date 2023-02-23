package com.xplaptop.admin.settting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaptop.common.entity.setting.Currency;
import com.xplaptop.common.entity.setting.GeneralSettingBag;
import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	
	@Autowired
	private SettingRepository settingRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	public List<Setting> getAllSettings() {
		return settingRepository.findAll();
	}
	
	public List<Currency> getAllCurrencies() {
		return currencyRepository.findAllByOrderByNameAsc();
	}

	public List<Setting> getCurrencySettings() {
		return settingRepository.findByCategory(SettingCategory.CURRENCY);
	}
	
	public GeneralSettingBag getGeneralSettingBag() {
		List<Setting> settings = new ArrayList<>();
		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		
		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}
	
	public List<Setting> getMailServerSetting() {
		return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> getMailTemplatesSetting() {
		return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}

	public List<Setting> getPaymentSetting() {
		return settingRepository.findByCategory(SettingCategory.PAYMENT);
	}
}
