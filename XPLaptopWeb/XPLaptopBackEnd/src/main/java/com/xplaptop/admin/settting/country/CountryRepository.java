package com.xplaptop.admin.settting.country;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.country.Country;


@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
	List<Country> findAllByOrderByNameAsc();

	Country findByCode(String string);
}
