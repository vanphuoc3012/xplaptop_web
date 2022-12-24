package com.xplaptop.common.entity.setting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "currencies")
public class Currency {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 64, nullable = false)
	@NonNull
	private String name;
	
	@Column(length = 3, nullable = false)
	@NonNull
	private String symbol;
	
	@Column(length = 4, nullable = false, unique = true)
	@NonNull
	private String code;
	
	@Override
	public String toString() {
		return this.name+" - "+this.code+" - "+this.symbol;
	}
}
