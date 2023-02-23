package com.xplaptop.common.entity.customer;

import com.xplaptop.common.entity.AbstractAddress;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.user.AuthenticationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends AbstractAddress {
	@Column(name = "email", unique = true, length = 45, nullable = false)
	private String email;
	
	@Column(nullable = false, length = 64)
	private String password;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	@Column(name = "verification_code", length = 64)
	private String verificationCode;
	
	private boolean enabled;
	
	@Column(name = "created_time")
	private Date createdTime;

	@Enumerated(EnumType.STRING)
	private AuthenticationType authenticationType;

	@Column(name = "reset_password_token", length = 64)
	private String resetPasswordToken;

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getFullAddress() {
		return addressLine1 + " " + city + " " + state + " " + country.getName();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getFullName() + ", ");
		sb.append(getPhoneNumber() + ", ");
		sb.append(getFullAddress());
		return sb.toString();
	}
}
