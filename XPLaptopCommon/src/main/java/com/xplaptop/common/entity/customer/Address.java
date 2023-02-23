package com.xplaptop.common.entity.customer;

import com.xplaptop.common.entity.AbstractAddress;
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
public class Address extends AbstractAddress {

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "default_address")
    private boolean defaultAddress;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFullAddress() {
        return addressLine1 + ", " + state + ", " + city + ", " + country.getName();
    }

    @Override
    public String toString() {
        return getFullName() + ", " + getPhoneNumber() + ", " + getFullAddress();
    }
}
