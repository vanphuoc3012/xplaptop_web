package com.xplaptop.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	
	@Autowired
	private SettingRepository settingRepository;
	
	public List<Setting> getListSettings() {
		
		return settingRepository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
}
