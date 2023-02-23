package com.xplaptop.shipping;

import com.xplaptop.common.entity.setting.ShippingRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    Optional<ShippingRate> findByCountry_IdAndState(Integer countryId, String state);
}
