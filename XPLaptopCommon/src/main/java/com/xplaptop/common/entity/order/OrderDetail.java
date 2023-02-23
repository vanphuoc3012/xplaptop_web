package com.xplaptop.common.entity.order;

import com.xplaptop.common.entity.IdBaseEntity;
import com.xplaptop.common.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends IdBaseEntity {

    private int quantity;

    private double productCost;
    private double shippingCost;
    private double unitPrice;

    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
