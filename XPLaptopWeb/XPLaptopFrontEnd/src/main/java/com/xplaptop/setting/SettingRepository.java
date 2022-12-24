package com.xplaptop.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.setting.Setting;
import com.xplaptop.common.entity.setting.SettingCategory;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String>{
	
	List<Setting> findByCategory(SettingCategory category);
	
	@Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
	List<Setting> findByTwoCategory(SettingCategory category1, SettingCategory category2);
}
