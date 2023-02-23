package com.xplaptop.common.entity.product;

import com.xplaptop.common.entity.IdBaseEntity;
import lombok.*;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "product_images")
public class ProductImage extends IdBaseEntity {
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
