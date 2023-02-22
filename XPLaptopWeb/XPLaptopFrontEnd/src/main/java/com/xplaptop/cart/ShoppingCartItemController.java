package com.xplaptop.cart;

import com.xplaptop.Utitlity;
import com.xplaptop.address.AddressService;
import com.xplaptop.common.entity.CartItem;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.setting.ShippingRate;
import com.xplaptop.common.exception.AddressNotFoundException;
import com.xplaptop.common.exception.CustomerNotFoundException;
import com.xplaptop.customer.CustomerService;
import com.xplaptop.shipping.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartItemController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ShippingRateService shippingRateService;

    @GetMapping("/cart")
    public String viewCart(ModelMap model,
                           HttpServletRequest request) {
            Customer customer = getAuthenticatedCustomer(request);
            List<CartItem> cartItems = cartService.listCartItems(customer);
        double estimatedTotal = cartItems.stream()
                .mapToDouble(c -> c.getSubtotal())
                .sum();

        model.put("cartItems", cartItems);
        model.put("estimatedTotal", estimatedTotal);
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utitlity.getEmailOfAuthenticatedCustomer(request);
        return customerService.findCustomerByEmail(email);
    }
}
