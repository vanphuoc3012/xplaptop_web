package com.xplaptop.order;

import com.xplaptop.checkout.CheckOutInfo;
import com.xplaptop.common.entity.CartItem;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.order.Order;
import com.xplaptop.common.entity.order.OrderDetail;
import com.xplaptop.common.entity.order.OrderStatus;
import com.xplaptop.common.entity.order.PaymentMethod;
import com.xplaptop.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Customer customer,
                             Address address,
                             List<CartItem> cartItemList,
                             PaymentMethod paymentMethod,
                             CheckOutInfo checkOutInfo) {
        Order newOrder = new Order();
        newOrder.setOrderTime(new Date());
        if(paymentMethod.equals(PaymentMethod.PAYPAL)) {
            newOrder.setOrderStatus(OrderStatus.PAID);
        } else {
            newOrder.setOrderStatus(OrderStatus.NEW);
        }
        newOrder.setCustomer(customer);
        newOrder.setProductCost(checkOutInfo.getProductCost());
        newOrder.setSubtotal(checkOutInfo.getProductTotal());
        newOrder.setShippingCost(checkOutInfo.getShippingCostTotal());
        newOrder.setTax(0.0f);
        newOrder.setTotal(checkOutInfo.getPaymentTotal());
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setDeliverDays(checkOutInfo.getDeliverDays());
        newOrder.setDeliverDate(checkOutInfo.getDeliverDate());

        if(address == null) {
            newOrder.copyAddressFromCustomer();
        } else {
            newOrder.copyShippingAddressFromAddressEntity(address);
        }
        for(CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.discountPrice());
            orderDetail.setProductCost(product.getCost());
            orderDetail.setSubtotal(cartItem.getSubtotal());
            orderDetail.setShippingCost(cartItem.getShippingCost());

            newOrder.addOrderDetail(orderDetail);
        }
        return orderRepository.save(newOrder);
    }
}
