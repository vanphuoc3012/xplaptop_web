package com.xplaptop.test.order;

import com.xplaptop.common.entity.order.Order;
import com.xplaptop.common.entity.order.OrderStatus;
import com.xplaptop.common.entity.order.OrderTrack;
import com.xplaptop.order.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testUpdateOrderTracks() {
        int orderId = 49;
        Order order = orderRepository.findById(orderId).get();

        OrderTrack track1 = new OrderTrack();
        track1.setOrder(order);
        track1.setUpdatedTime(new Date());
        track1.setStatus(OrderStatus.PACKAGED);
        track1.setNotes(OrderStatus.NEW.defaultDescription());

        order.addOrderTrack(track1);

        Order savedOrder = orderRepository.save(order);
        Assertions.assertThat(savedOrder.getOrderTracks().size()).isGreaterThan(1);
        System.out.println(savedOrder.getOrderTracks().size());
    }
}
