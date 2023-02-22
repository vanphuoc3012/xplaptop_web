package com.xplaptop.shipping;

import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.setting.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateService {
    @Autowired
    private ShippingRateRepository shippingRateRepository;

    public ShippingRate getShippingRateForCustomer(Customer customer) {
        String state = customer.getState();
        if(state == null || state.isEmpty()) {
            state = customer.getCity();
        }
        return shippingRateRepository.findByCountry_IdAndState(customer.getCountry().getId(), state);
    }

    public ShippingRate getShippingRateForAddress(Address address) {
        String state = address.getState();
        if(state == null || state.isEmpty()) {
            state = address.getCity();
        }
        return shippingRateRepository.findByCountry_IdAndState(address.getCountry().getId(), state);
    }
}
