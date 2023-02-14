package com.xplaptop.admin.settting.shippingrate;

import com.xplaptop.admin.settting.country.CountryRepository;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.setting.ShippingRate;
import com.xplaptop.common.exception.ShippingRateAlreadyExistsException;
import com.xplaptop.common.exception.ShippingRateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingRateService {
    private final static int SHIPPING_RATE_PER_PAGE = 5;

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getAllCountry() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public Page<ShippingRate> getByPage(Integer pageNumber, String sortDir, String sortField, String keyword) {
        Sort sort = Sort.by(sortField);
        if(sortDir.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, SHIPPING_RATE_PER_PAGE, sort);
        if (keyword == null) return shippingRateRepository.findAll(pageable);
        return shippingRateRepository.findAll(keyword, pageable);
    }

    public String save(ShippingRate shippingRate) throws ShippingRateAlreadyExistsException, ShippingRateNotFoundException {
        boolean isCreateNew = shippingRate.getId() == null;
        if(isCreateNew) {
            Optional<ShippingRate> optional = shippingRateRepository.findByCountry_IdAndState(shippingRate.getCountry().getId(), shippingRate.getState());
            if(optional.isPresent()) throw new ShippingRateAlreadyExistsException("There's already a rate for the destination " + shippingRate.getCountry().getName() + ", " + shippingRate.getState());
            shippingRateRepository.save(shippingRate);
            return "New shipping rate has been added successfully";
        } else {
            ShippingRate shippingRateInDB = findShippingRateById(shippingRate.getId());
            shippingRateRepository.save(shippingRate);
            return "Shipping Rate has been updated";
        }
    }

    public ShippingRate findShippingRateById(Integer id) throws ShippingRateNotFoundException {
        return shippingRateRepository.findById(id).orElseThrow(
                () -> new ShippingRateNotFoundException("No Shipping Rate found with ID: " + id)
        );
    }

    public void deleteShippingRate(Integer id) throws ShippingRateNotFoundException {
        ShippingRate shippingRate = findShippingRateById(id);
        shippingRateRepository.delete(shippingRate);
    }

    public String updateCODSupported(Integer id) throws ShippingRateNotFoundException {
        ShippingRate shippingRate = findShippingRateById(id);
        boolean codSupported = shippingRate.isCodSupported();
        String message;
        if(codSupported) {
            shippingRate.setCodSupported(false);
            message = "Successfully updated Shipping Rate (ID: " + id+ ") to not support COD";
        } else {
            shippingRate.setCodSupported(true);
            message = "Successfully updated Shipping Rate (ID: " + id+ ") to support COD";
        }
        shippingRateRepository.save(shippingRate);
        return message;
    }
}
