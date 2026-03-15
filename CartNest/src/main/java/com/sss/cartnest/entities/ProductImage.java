package com.sss.cartnest.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="productimages")
public class ProductImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int image_id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Product product;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String image_url;
	
	public ProductImage() {
		// TODO Auto-generated constructor stub
	}

	public ProductImage(Product product, String image_url) {
		super();
		this.product = product;
		this.image_url = image_url;
	}

	public int getImage_id() {
		return image_id;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
}
