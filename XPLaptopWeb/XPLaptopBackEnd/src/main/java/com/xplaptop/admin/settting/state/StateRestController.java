package com.xplaptop.admin.settting.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xplaptop.admin.settting.country.StateRepository;
import com.xplaptop.common.entity.country.State;

@RestController
public class StateRestController {
	@Autowired private StateRepository stateRepo;
	
	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
//		List<State> listStates = stateRepo.
		
		return null;
	}
}
