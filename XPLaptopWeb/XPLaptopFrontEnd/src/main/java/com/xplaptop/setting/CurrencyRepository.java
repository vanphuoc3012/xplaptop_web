package com.xplaptop.setting;

import com.xplaptop.common.entity.setting.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
