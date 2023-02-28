package com.xplaptop.common.entity.setting;

import com.xplaptop.common.entity.IdBaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "currencies")
public class Currency extends IdBaseEntity {
	
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
