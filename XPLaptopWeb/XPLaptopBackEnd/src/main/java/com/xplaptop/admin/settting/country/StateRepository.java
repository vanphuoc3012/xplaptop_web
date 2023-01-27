package com.xplaptop.admin.settting.country;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;

@Repository
public interface StateRepository extends CrudRepository<State, Integer>{
	List<State> findAllByOrderByNameAsc();
	
	List<State> findByCountryOrderByNameAsc(Country country);
}
