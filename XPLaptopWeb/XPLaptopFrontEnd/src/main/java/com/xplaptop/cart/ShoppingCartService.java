package com.xplaptop.cart;

import com.xplaptop.common.entity.CartItem;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.product.Product;
import com.xplaptop.common.exception.ShoppingCartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Integer updatedQuantity = quantity;
        Product product = new Product(productId);
        CartItem cartItem = cartItemRepository.findByCustomer_IdAndProduct_Id(customer.getId(), productId);
        if(cartItem != null) {
            updatedQuantity = cartItem.getQuantity() + quantity;
            if(updatedQuantity > 5) {
                throw new ShoppingCartException("You can add maximum 5 items of this product into your cart! You already have "
                        + cartItem.getQuantity() + " items of this product in your cart");
            }
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCustomer(customer);

        }
        cartItem.setQuantity(updatedQuantity);
        cartItemRepository.save(cartItem);
        return updatedQuantity;
    }

    public List<CartItem> listCartItems(Customer customer) {
        return cartItemRepository.findAllByCustomer_Id(customer.getId());
    }

}
