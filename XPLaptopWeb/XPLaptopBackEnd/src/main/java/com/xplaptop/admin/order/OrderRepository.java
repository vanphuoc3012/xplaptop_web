package com.xplaptop.admin.order;

import com.xplaptop.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE CONCAT(o.firstName,' ', o.lastName,' ', o.addressLine1,' ', o.addressLine2" +
            ", ' ', o.paymentMethod, ' ', o.city, ' ', o.state, ' ', o.country, ' ', o.postalCode, ' ', o.orderStatus) LIKE %?1%")
    Page<Order> findAll(String keyword, Pageable pageable);

    Page<Order> findAll(Pageable pageable);
}
