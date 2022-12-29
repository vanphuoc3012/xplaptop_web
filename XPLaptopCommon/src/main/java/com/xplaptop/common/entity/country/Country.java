package com.xplaptop.common.entity.country;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "countries")
@ToString
public class Country {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128,nullable = false)
	@NonNull
	private String name;
	
	@Column(length = 10, nullable = false)
	@NonNull
	private String code;
	
	@OneToMany(mappedBy = "country")
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<State> states = new HashSet<>();

	public Country(Integer id) {
		this.id = id;
	}

	public Country(@NonNull String code) {
		this.code = code;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		return Objects.equals(code, other.code);
	}

}
