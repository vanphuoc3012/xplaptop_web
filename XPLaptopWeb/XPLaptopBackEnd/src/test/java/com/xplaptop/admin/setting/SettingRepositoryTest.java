package com.xplaptop.admin.setting;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.admin.settting.SettingRepository;
import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingCategory;

@DataJpaTest(showSql = false)
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SettingRepositoryTest {
	@Autowired
	private SettingRepository settingRepository;
	
	@Test
	public void createSettingTest() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting currencySymbol = new Setting("CURRENCY_SYMBOL", "VND", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "after", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting currencyDigits = new Setting("CURRENCY_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		
		settingRepository.saveAll(Arrays.asList(currencyId, currencySymbol, symbolPosition, decimalPointType, currencyDigits, thousandsPointType));
		
	}
	
	@Test
	public void listByCategoryTest() {
		List<Setting> findByCategory = settingRepository.findByCategory(SettingCategory.GENERAL);
		
		for(Setting s : findByCategory) {
			System.out.println(s.getKey());
		}
	}
}
