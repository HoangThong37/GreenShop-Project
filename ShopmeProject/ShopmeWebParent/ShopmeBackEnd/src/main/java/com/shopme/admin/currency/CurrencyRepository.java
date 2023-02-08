package com.shopme.admin.currency;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

}
