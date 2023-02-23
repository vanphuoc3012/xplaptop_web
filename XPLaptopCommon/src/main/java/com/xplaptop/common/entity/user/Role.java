package com.xplaptop.common.entity.user;

import com.xplaptop.common.entity.IdBaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Role extends IdBaseEntity {
	@Column(length = 50, nullable = false, unique = true)
	@NonNull
	private String name;
	
	@Column(length = 150, nullable = false)
	@NonNull
	private String description;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public Role(Integer id) {
		super();
		this.id = id;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	

}
