package com.xplaptop.common.entity.order;

import com.xplaptop.common.entity.AbstractAddress;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends AbstractAddress {
    @Column(nullable = false, length = 45)
    private String country;

    private Date orderTime;

    private double shippingCost;
    private double productCost;
    private double subtotal;
    private double tax;
    private double total;

    private int deliverDays;
    private Date deliverDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // use list here to maintain the order of orderTracks
    private List<OrderTrack> orderTracks = new ArrayList<>();

    public String getDestination() {
        StringBuilder sb = new StringBuilder();
        if(!city.isEmpty()) sb.append(city + ", ");
        if(!state.isEmpty()) sb.append(state + ", ");
        sb.append(country);
        return sb.toString();
    }

    public String getShippingAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(addressLine1 + ", ");
        if(checkNotNullAndNotEmpty(addressLine2)) sb.append(addressLine2+ ", ");
        if(checkNotNullAndNotEmpty(city)) sb.append(city + ", ");
        if(checkNotNullAndNotEmpty(state)) sb.append(state + ", ");
        sb.append(country);
        return sb.toString();
    }

    private boolean checkNotNullAndNotEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    public void copyAddressFromCustomer() {
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setCity(customer.getCity());
        setCountry(customer.getCountry().getName());
        setPostalCode(customer.getPostalCode());
        setState(customer.getState());
    }

    public void copyShippingAddressFromAddressEntity(Address address) {
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setPhoneNumber(address.getPhoneNumber());
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setCity(address.getCity());
        setCountry(address.getCountry().getName());
        setPostalCode(address.getPostalCode());
        setState(address.getState());
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
    }

    public void addOrderTrack(OrderTrack orderTrack) {
        orderTracks.add(orderTrack);
    }
}
