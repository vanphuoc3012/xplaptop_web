package com.xplaptop.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.xplaptop.admin.settting.CurrencyRepository;
import com.xplaptop.common.entity.setting.Currency;

@DataJpaTest(showSql = true)
@Rollback(false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CurrencyRepositoryTest {
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Test
	public void testCreateCurrency() {
		currencyRepository.save(new Currency("Euro", "€", "EUR"));
		currencyRepository.save(new Currency("Japanese yen", "¥", "JPY"));
		currencyRepository.save(new Currency("Australian dollar", "A$", "AUD"));
		currencyRepository.save(new Currency("Renminbi", "¥", "CNY"));
		currencyRepository.save(new Currency("Sterling", "£", "GBP"));
		currencyRepository.save(new Currency("Canadian dollar", "C$", "CAD"));
		currencyRepository.save(new Currency("Singapore dollar", "S$", "SGD"));
		currencyRepository.save(new Currency("South Korean won", "₩", "KRW"));
		currencyRepository.save(new Currency("Russian ruble", "₽", "RUB"));
		currencyRepository.save(new Currency("Thai baht", "฿", "THB"));
		currencyRepository.save(new Currency("Vietnamese đồng", "₫", "VND"));
	}
	
	@Test
	public void testFindAll() {
		List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
		for(Currency c : listCurrencies) {
			System.out.println(c.getName());
		}
		
		assertThat(listCurrencies).hasSizeGreaterThan(1);
	}
}
