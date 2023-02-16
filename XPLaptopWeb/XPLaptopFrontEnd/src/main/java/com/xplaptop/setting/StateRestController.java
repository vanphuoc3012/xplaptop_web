package com.xplaptop.setting;

import com.xplaptop.common.entity.country.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/states/list_by_country/{countryId}")
    public List<State> getAllStateFromCountry(@PathVariable(name = "countryId") Integer countryId) {
        return stateRepository.findAllByCountry_Id(countryId);
    }
}
