package com.xplaptop.setting;

import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
	
	@Autowired
	private SettingRepository settingRepository;
	
	public List<Setting> getListSettings() {
		
		return settingRepository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}

	public EmailSettingBag getEmailSettingBag() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

		return new EmailSettingBag(settings);
	}

	public CurrencySettingBag getCurrencySettingBag() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);

		return new CurrencySettingBag(settings);
	}
}
