package com.xplaptop.admin.settting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.setting.Currency;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Integer>{
	
	List<Currency> findAllByOrderByNameAsc();
}
