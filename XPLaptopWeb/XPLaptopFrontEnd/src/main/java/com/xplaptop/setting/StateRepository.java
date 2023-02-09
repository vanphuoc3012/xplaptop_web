package com.xplaptop.setting;

import com.xplaptop.common.entity.country.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {

    List<State> findAllByCountry_Id(Integer countryId);
}
