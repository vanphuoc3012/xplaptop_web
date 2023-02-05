package com.xplaptop.setting;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xplaptop.common.entity.country.Country;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer>{
    List<Country> findAllByOrderByNameAsc();

    Country findByCode(String countryCode);
}
