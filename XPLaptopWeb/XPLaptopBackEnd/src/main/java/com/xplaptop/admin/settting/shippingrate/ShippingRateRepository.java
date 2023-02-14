package com.xplaptop.admin.settting.shippingrate;

import com.xplaptop.common.entity.setting.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    Page<ShippingRate> findAll(Pageable pageable);
    @Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %?1% OR sr.state LIKE %?1%")
    Page<ShippingRate> findAll(String keyword, Pageable pageable);

    Optional<ShippingRate> findByCountry_IdAndState(Integer countryId, String state);
}
