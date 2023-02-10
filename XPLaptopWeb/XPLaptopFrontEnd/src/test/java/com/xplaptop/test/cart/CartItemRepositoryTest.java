package com.xplaptop.test.cart;

import com.xplaptop.cart.CartItemRepository;
import com.xplaptop.common.entity.CartItem;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTest {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testSaveCartItem() {
        int customerId = 10;
        int productId = 88;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem cartItem = new CartItem().builder().customer(customer)
                .product(product)
                .quantity(1)
                .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        Assertions.assertThat(savedCartItem.getId()).isGreaterThan(0);
    }

    @Test
    public void testSave2CartItem() {
        int customerId1 = 14;
        int productId1 = 83;
        Customer customer1 = entityManager.find(Customer.class, customerId1);
        Product product1 = entityManager.find(Product.class, productId1);
        CartItem cartItem1 = new CartItem().builder().customer(customer1)
                .product(product1)
                .quantity(6)
                .build();
        int customerId2 = 15;
        int productId2 = 76;
        Customer customer2 = entityManager.find(Customer.class, customerId2);
        Product product2 = entityManager.find(Product.class, productId2);
        CartItem cartItem2 = new CartItem().builder().customer(customer2)
                .product(product2)
                .quantity(3)
                .build();

        List<CartItem> cartItems = cartItemRepository.saveAll(List.of(cartItem1, cartItem2));
        Assertions.assertThat(cartItems.size()).isGreaterThan(1);
    }
}
