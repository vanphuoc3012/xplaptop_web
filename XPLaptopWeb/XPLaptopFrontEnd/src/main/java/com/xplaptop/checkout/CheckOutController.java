package com.xplaptop.checkout;

import com.xplaptop.Utitlity;
import com.xplaptop.address.AddressService;
import com.xplaptop.cart.ShoppingCartService;
import com.xplaptop.common.entity.CartItem;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.setting.ShippingRate;
import com.xplaptop.customer.CustomerService;
import com.xplaptop.shipping.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckOutController {

    @Autowired
    private CheckOutService checkOutService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ShippingRateService shippingRateService;
    @Autowired
    private ShoppingCartService cartService;

    @GetMapping("/checkout")
    public String showCheckOutPage(ModelMap model,
                                   HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);

        boolean usePrimaryAddressAsDefault = addressService.usePrimaryAsDefaultAddress(customer);
        Optional<ShippingRate> optShippingRate;
        if(usePrimaryAddressAsDefault) {
            model.put("shippingAddress", customer.toString());
            optShippingRate = shippingRateService.getShippingRateForCustomer(customer);
        } else {
            Address defaultAddress = addressService.getDefaultAddress(customer);
            model.put("shippingAddress", defaultAddress.toString());
            optShippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }
        if(optShippingRate.isEmpty()) {
            return "redirect:/cart";
        }
        CheckOutInfo checkOutInfo = checkOutService.prepareCheckOut(cartItems, optShippingRate.get());

        model.put("checkOutInfo", checkOutInfo);
        model.put("cartItems", cartItems);
        return "checkout/checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utitlity.getEmailOfAuthenticatedCustomer(request);
        return customerService.findCustomerByEmail(email);
    }
}
