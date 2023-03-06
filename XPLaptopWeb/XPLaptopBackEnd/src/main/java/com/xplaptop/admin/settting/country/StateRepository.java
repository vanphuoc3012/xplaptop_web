package com.xplaptop.admin.settting.country;

import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Integer>{
	List<State> findAllByOrderByNameAsc();
	
	List<State> findByCountryOrderByNameAsc(Country country);

    List<State> findByCountry_NameOrderByNameAsc(String countryName);
}
