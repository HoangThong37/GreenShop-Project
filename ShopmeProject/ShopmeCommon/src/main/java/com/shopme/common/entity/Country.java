package com.shopme.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {    // country 1-n state

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 45)
	private String name;

	@Column(nullable = false, length = 5)
	private String code;

	// bên 1 - country => list-n(states)
	@OneToMany(mappedBy = "country")
	private Set<State> states;
	
	// n.customer - 1.country
	@OneToMany(mappedBy = "country")
	private Set<Customer> customers;


	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}
	
	public Country(String name) {
		this.name = name;
	}
	
	public Country(Integer id) {
		this.id = id;
	}
	
	public Country() {
	}
	

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

//	public Set<State> getStates() {
//		return states;
//	}
//
//	public void setStates(Set<State> states) {
//		this.states = states;
//	}
	
	


}
