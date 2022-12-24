package com.xplaptop.test.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingCategory;
import com.xplaptop.setting.SettingRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SettingRepositoryTest {
	
	@Autowired
	private SettingRepository settingRepository;
	
	@Test
	public void testFindBy2Category() {
		List<Setting> findByTwoCategory = settingRepository.findByTwoCategory(SettingCategory.CURRENCY, SettingCategory.GENERAL);
		
		findByTwoCategory.stream()
							.forEach(s -> System.out.println(s.getKey() +"-"+ s.getValue()));
		assertThat(findByTwoCategory).hasSizeGreaterThan(1);
	}

}
