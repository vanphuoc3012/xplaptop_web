package com.xplaptop.common.entity.product;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.xplaptop.common.entity.Brand;
import com.xplaptop.common.entity.Category;

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
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, unique = true)
	@NonNull
	private String name;
	
	@Column(nullable = false, unique = true)
	@NonNull
	private String alias;
	
	@Column(length = 1024, nullable = false)
	private String shortDescription;
	
	@Column(length = 12096, nullable = false)
	private String fullDescription;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("name asc")
	private Set<ProductDetail> productDetails = new HashSet<>();
	
	private String mainImage;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> productImages = new HashSet<>();
	
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	
	@Column(nullable = false)
	private boolean enabled;	
	private boolean inStock;
	
	private Double price;
	private Double discountPercent;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@Column(nullable = false)
	private double length;
	@Column(nullable = false)
	private double width;	
	@Column(nullable = false)
	private double height;
	@Column(nullable = false)
	private double weight;
	
	private double averageRating;
	private int reviewCount;
	
	private Double cost;
	
	public Product(Integer id, @NonNull String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Product(Integer productId) {
		this.id = productId;
	}

	public void addExtrasImage(String imageName) {
		this.productImages.add(new ProductImage(imageName, this));
	}
	
	public void addDetail(String datailName, String datailValue) {
		this.productDetails.add(new ProductDetail(datailName, datailValue, this));
	}
	
	@Transient
	public String imagePath() {
		if(id == null || mainImage == null || mainImage.isEmpty()) {
			return "/images/productDefault.png";
		} else {
			return "/product-images/" + this.id + "/" + this.mainImage;
		}
	}
	
	@Transient
	public ProductDetail getDetailByName(String detailName) {
		for(ProductDetail pd : this.productDetails) {
			if(pd.getName().equals(detailName)) {
				return pd;
			}
		}
		return null;
		
	}
	
	@Transient
	public ProductImage getProductImageByName(String fileName) {
		for(ProductImage pi : this.productImages) {
			if(pi.getName().equals(fileName)) {
				return pi;
			}
		}
		return null;
	}
	
	@Transient
	public double discountPrice() {
		if(this.discountPercent > 0) {
			return price * ((100 - discountPercent) / 100);
		}
		return this.price;
	}
	
	@Transient
	public String getShortName() {
		if(this.name.length() > 70) {
			return this.name.substring(0, 67) + "...";
		}
		return this.name;
	}
}
