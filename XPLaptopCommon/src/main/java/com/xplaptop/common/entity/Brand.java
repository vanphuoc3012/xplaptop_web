package com.xplaptop.common.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "brands")
public class Brand extends IdBaseEntity {

	@Column(length = 128, nullable = false, unique = true)
	@NonNull
	private String name;
	
	@Column(length = 128)
	private String logo;
	
	@ManyToMany
	@JoinTable(
			name = "brands_categories",
			joinColumns = @JoinColumn(name = "brand_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id")
			)
	private Set<Category> categories = new HashSet<>();
	
	
	
	public String imagePath() {
		if(this.id == null || this.logo == null || this.logo.isEmpty()) {
			return "/images/imageicon.png";
		}
		return "/brand-logos/" + this.id + "/" + this.logo;
	}

	public Brand(Integer id) {
		super();
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + "]";
	}

	public Brand(Integer id, @NonNull String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
}
