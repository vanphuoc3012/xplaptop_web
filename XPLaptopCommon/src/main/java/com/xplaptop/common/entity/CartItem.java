package com.xplaptop.common.entity;

import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.product.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends IdBaseEntity{

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Transient
    private double shippingCost;
    public Double getSubtotal() {
        return product.discountPrice() * quantity;
    }

    @Transient
    public double getShippingCost() {
        return shippingCost;
    }

    @Transient
    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }
}
