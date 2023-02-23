package com.xplaptop.order;

import com.xplaptop.checkout.CheckOutInfo;
import com.xplaptop.common.entity.CartItem;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.order.Order;
import com.xplaptop.common.entity.order.OrderStatus;
import com.xplaptop.common.entity.order.PaymentMethod;
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
        newOrder.setOrderStatus(OrderStatus.NEW);
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
        }

        return orderRepository.save(newOrder);
    }
}
