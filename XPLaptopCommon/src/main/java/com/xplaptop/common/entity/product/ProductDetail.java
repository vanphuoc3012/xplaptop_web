package com.xplaptop.common.entity.product;

import com.xplaptop.common.entity.IdBaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "product_details")
public class ProductDetail extends IdBaseEntity {
	@Column(nullable = false)
	@NonNull
	private String name;
	
	@Column(length = 512, nullable = false)
	@NonNull
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	@NonNull
	private Product product; 
}
