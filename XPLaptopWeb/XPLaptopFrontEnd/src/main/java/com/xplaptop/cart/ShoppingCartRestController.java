package com.xplaptop.cart;

import com.xplaptop.Utitlity;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.exception.CustomerNotFoundException;
import com.xplaptop.common.exception.ProductNotFoundException;
import com.xplaptop.common.exception.ShoppingCartException;
import com.xplaptop.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable(name = "quantity") Integer quantity,
                                   HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);

            return updatedQuantity + " item(s) of this product were added to your shopping cart.";
        } catch (CustomerNotFoundException e) {
            return "You need login to add this product to cart.";
        } catch (ShoppingCartException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utitlity.getEmailOfAuthenticatedCustomer(request);
        if(email == null) throw new CustomerNotFoundException("No authenticated customer");
        return customerService.findCustomerByEmail(email);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable(name = "productId") Integer productId,
                                   @PathVariable(name = "quantity") Integer quantity,
                                   HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);
            double subtotal = cartService.updateQuantity(productId, quantity, customer);

            return String.valueOf(subtotal);
        } catch (CustomerNotFoundException e) {
            return "You must login to change quantity of this product";
        } catch (ProductNotFoundException e) {
            return "Product not found";
        }
    }
}
