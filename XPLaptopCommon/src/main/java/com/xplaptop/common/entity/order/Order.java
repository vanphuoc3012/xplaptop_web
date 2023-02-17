package com.xplaptop.common.entity.order;

import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

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

    @Column(nullable = false, length = 45)
    private String country;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

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

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public String getDestination() {
        StringBuilder sb = new StringBuilder();
        if(!city.isEmpty()) sb.append(city + ", ");
        if(!state.isEmpty()) sb.append(state + ", ");
        sb.append(country);
        return sb.toString();
    }
}
