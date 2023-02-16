package com.xplaptop.common.entity.customer;

import com.xplaptop.common.entity.country.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "phone_number", nullable = false, length = 45)
    private String phoneNumber;

    @Column(name = "address_line", nullable = false, length = 128)
    private String addressLine;

    @Column(nullable = false, length = 45)
    private String city;

    @Column(nullable = false, length = 45)
    private String state;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "default_address")
    private boolean defaultAddress;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFullAddress() {
        return addressLine + " " + state + " " + city + " " + country.getName();
    }
}
