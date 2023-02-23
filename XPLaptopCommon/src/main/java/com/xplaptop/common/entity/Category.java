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
@Table(name = "categories")
public class Category extends IdBaseEntity {
	
	@Column(length = 128, nullable = false, unique = true)
	@NonNull
	private String name;
	
	@Column(length = 128, nullable = false, unique = true)
	@NonNull
	private String alias;
	
	@Column(length = 128)
	private String image;
	
	private boolean enabled;
	
	@Column(length =  256)
	private String allParentIds;
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	@OrderBy("name ASC")
	private Set<Category> children = new HashSet<>();

	public Category(@NonNull String name, @NonNull String alias, String image, boolean enabled,
			Category parent, Set<Category> children) {
		this.name = name;
		this.alias = alias;
		this.image = image;
		this.enabled = enabled;
		this.parent = parent;
		this.children = children;
	}

	public Category(@NonNull String name, @NonNull String alias, String image, boolean enabled,
			Category parent) {
		this.name = name;
		this.alias = alias;
		this.image = image;
		this.enabled = enabled;
		this.parent = parent;
	}

	public Category(@NonNull String name, @NonNull String alias, String image, boolean enabled,
			Set<Category> children) {
		this.name = name;
		this.alias = alias;
		this.image = image;
		this.enabled = enabled;
		this.children = children;
	}

	public Category(@NonNull String name, Category parent) {
		this.name = name;
		this.alias = name.toLowerCase();
		this.image = "imageicon.png";
		this.parent = parent;
	}
	
	public Category(Integer id, @NonNull String name) {
		this.id = id;
		this.name = name;
		this.alias = name.toLowerCase();
		this.image = "imageicon.png";
	}

	public Category(Integer id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public String imagePath() {
		if(this.id == null || this.image == null) {
			return "/images/imageicon.png";
		}
		return "/category-images/" + this.id + "/" + this.image;
	}
	
	public static Category copy(Category category) {
		Category cat = new Category();
		cat.setId(category.getId());
		cat.setName(category.getName());
		cat.setAlias(category.getAlias());
		cat.setImage(category.getImage());
		cat.setEnabled(category.isEnabled());
		cat.setParent(category.getParent());
		cat.setChildren(category.getChildren());
	
		return cat;
	}
	
}
