package com.xplaptop.common.entity.country;

import com.xplaptop.common.entity.IdBaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "countries")
@ToString
public class Country extends IdBaseEntity {
	
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
