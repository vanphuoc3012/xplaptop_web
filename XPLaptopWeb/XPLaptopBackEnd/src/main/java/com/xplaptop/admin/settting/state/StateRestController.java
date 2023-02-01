package com.xplaptop.admin.settting.state;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xplaptop.admin.settting.country.StateRepository;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;

@RestController
public class StateRestController {
	@Autowired private StateRepository stateRepo;
	
	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		List<State> listStates = stateRepo.findByCountryOrderByNameAsc(new Country(countryId));
		
		return listStates.stream().map(s -> new StateDTO(s.getId(), s.getName()))
				.collect(Collectors.toList());
	}
	
	@PostMapping("/states/save")
	public String saveState(@RequestBody State state) {
		State savedState = stateRepo.save(state);
		return String.valueOf(savedState.getId());
	}
	
	@GetMapping("/states/delete/{id}")
	public void deleteState(@PathVariable("id") Integer id) {
		stateRepo.deleteById(id);
	}
}
