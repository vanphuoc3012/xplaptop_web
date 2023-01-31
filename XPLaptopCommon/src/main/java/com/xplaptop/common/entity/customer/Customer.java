package com.xplaptop.common.entity.customer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xplaptop.common.entity.country.Country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
@ToString
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "email", unique = true, length = 45, nullable = false)
	private String email;
	
	@Column(nullable = false, length = 64)
	private String password;
	
	@Column(name = "first_name", nullable = false, length = 45)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 45)
	private String lastName;
	
	@Column(name = "phone_number", nullable = false, length = 45)
	private String phoneNumber;
	
	@Column(name = "address_line1", nullable = false, length = 64)
	private String addressLine1;
	
	@Column(name = "address_line2", length = 64)
	private String addressLine2;
	
	@Column(nullable = false, length = 45)
	private String city;
	
	@Column(nullable = false, length = 45)
	private String state;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	@Column(name = "postal_code", nullable = false, length = 10)
	private String postalCode;
	
	@Column(name = "verification_code", length = 64)
	private String verificationCode;
	
	private boolean enabled;
	
	@Column(name = "created_time")
	private Date createdTime;

}
