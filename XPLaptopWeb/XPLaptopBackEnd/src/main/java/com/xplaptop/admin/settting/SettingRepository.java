package com.xplaptop.admin.settting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingCategory;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String>{
	
	List<Setting> findByCategory(SettingCategory category);
}
