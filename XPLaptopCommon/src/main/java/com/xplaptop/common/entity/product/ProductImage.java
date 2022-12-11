package com.xplaptop.common.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "product_images")
public class ProductImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false)
	@NonNull
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	@NonNull
	private Product product;
	
	@Transient
	public String extraImagePath() {
		if(id == null || name == null || name.isEmpty()) {
			return "/images/productDefault.png";
		} else {
			return "/product-images/" + product.getId() + "/extra-images/" + this.name;
		}
	}
}
