package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.CountryRepository;
import com.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {
	
	@Autowired
	public CountryRepository repo;
	
	@Test
	public void testCreateCountry() {
		Country country1 = repo.save(new Country("China", "CN"));
		Country country2 = repo.save(new Country("VietNam", "VN"));
		Country country3 = repo.save(new Country("Mỹ", "usd"));
		assertThat(country3).isNotNull();
		assertThat(country3.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCountries() {
		List<Country> listCountries = repo.findAllByOrderByNameAsc();
		listCountries.forEach(System.out::println);
		
		assertThat(listCountries.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateCountries() {
		Integer id = 1;
		String name = "Republic of India";
		Country country = repo.findById(id).get();
		country.setName(name);
		Country updateCountry = repo.save(country);
		assertThat(updateCountry.getName()).isEqualTo(name);		
	}	
	
	@Test
	public void testGetCountries() {
		Integer id = 3;
		Country country = repo.findById(id).get();
		// country.setName(name);
		//Country updateCountry = repo.save(country);
		assertThat(country).isNotNull();		
	}
	
	@Test
	public void testDeleteCountries() {
		Integer id = 3;
		repo.deleteById(id);
		Optional<Country> findById = repo.findById(id);
		//Country updateCountry = repo.save(country);
		assertThat(findById.isEmpty());		
	}
}
