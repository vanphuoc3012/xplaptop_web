package com.xplaptop.cart;

import com.xplaptop.common.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findAllByCustomer_Id(Integer customerId);

    CartItem findByCustomer_IdAndProduct_Id(Integer customerId, Integer productId);

    void deleteByCustomer_IdAndProduct_Id(Integer customerId, Integer productId);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
    void updateQuantity(Integer quantity, Integer customerId, Integer productId);
}
