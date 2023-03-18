package com.shopme.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    // country : quốc gia
	// findAllByOrderByNameAsc
	public List<Country> findAllByOrderByNameAsc();
}
