package com.xplaptop.common.entity.country;

import com.xplaptop.common.entity.IdBaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "states")
public class State extends IdBaseEntity {
	@Column(length = 128)
	@NonNull
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	@NonNull
	private Country country;

	public State(Integer id) {
		this.id = id;
	}
	
	
}
