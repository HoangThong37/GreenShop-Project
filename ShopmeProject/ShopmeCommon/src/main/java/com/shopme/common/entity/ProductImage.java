package com.shopme.common.entity;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
	 
	 @Column(length = 256, nullable = false, unique = true)
     private String name;
     
     @ManyToOne
	 @JoinColumn(name="product_id")
	 private Product product;
     
  	public ProductImage() {

 	}
     
 	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
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
	
	@Transient
	public String getImagePath() {
		return "/product-images/" + product.getId() + "/extras/" + this.name;
	}
}
