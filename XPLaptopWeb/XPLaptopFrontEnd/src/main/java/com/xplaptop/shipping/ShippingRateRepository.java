package com.xplaptop.shipping;

import com.xplaptop.common.entity.setting.ShippingRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    ShippingRate findByCountry_IdAndState(Integer countryId, String state);
}
