package com.xplaptop.checkout;

import com.xplaptop.Utitlity;
import com.xplaptop.address.AddressService;
import com.xplaptop.cart.ShoppingCartService;
import com.xplaptop.checkout.paypal.PayPalService;
import com.xplaptop.common.entity.CartItem;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.entity.order.Order;
import com.xplaptop.common.entity.order.PaymentMethod;
import com.xplaptop.common.entity.setting.ShippingRate;
import com.xplaptop.common.exception.PayPalApiException;
import com.xplaptop.customer.CustomerService;
import com.xplaptop.order.OrderService;
import com.xplaptop.setting.CurrencySettingBag;
import com.xplaptop.setting.EmailSettingBag;
import com.xplaptop.setting.PaymentSettingBag;
import com.xplaptop.setting.SettingService;
import com.xplaptop.shipping.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private PayPalService payPalService;

    @GetMapping("/checkout")
    public String showCheckOutPage(ModelMap model,
                                   HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);
        CurrencySettingBag currencySettingBag = settingService.getCurrencySettingBag();

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
        String currencyCode = settingService.getCurrencyCode();
        PaymentSettingBag paymentSettingBag = settingService.getPaymentSettingBag();
        String paypalClientID = paymentSettingBag.getClientID();

        model.put("paypalClientID", paypalClientID);
        model.put("customer", customer);
        model.put("currencyCode", currencyCode);
        model.put("checkOutInfo", checkOutInfo);
        model.put("cartItems", cartItems);
        model.put("paymentTotal", Utitlity.formatCurrencyForPayPal(checkOutInfo.getPaymentTotal(), currencySettingBag));
        return "checkout/checkout";
    }

    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request,
                             ModelMap model) {
        String payment = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(payment);
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);
        boolean usePrimaryAddressAsDefault = addressService.usePrimaryAsDefaultAddress(customer);
        Optional<ShippingRate> optShippingRate;
        Address defaultAddress = addressService.getDefaultAddress(customer);

        if(usePrimaryAddressAsDefault) {
            optShippingRate = shippingRateService.getShippingRateForCustomer(customer);
        } else {
            optShippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }

        CheckOutInfo checkOutInfo = checkOutService.prepareCheckOut(cartItems, optShippingRate.get());
        Order order = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkOutInfo);
        try {
            sendOrderConfirmationEmail(request, customer, order);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.put("pageTitle", "Checkout result");
            model.put("error", true);
            model.put("message", "An error has been occur, please try again later.");
            return "checkout/checkout_completed";
        }
        cartService.deleteByCustomer(customer);

        model.put("pageTitle", "Checkout result");
        model.put("error", false);
        model.put("message", "Your Order has been completed!");
        return "checkout/checkout_completed";
    }

    @PostMapping("/process_paypal_order")
    public String processPayPalOrder(HttpServletRequest request, ModelMap model) {
        String orderId = request.getParameter("orderId");
        String pageTitle = "Checkout result";
        String message;
        try {
            if(payPalService.validateOrder(orderId)) {
                return placeOrder(request, model);
            } else {
                message = "ERROR: Transaction could not be completed because order information is invalid";
            }
        } catch (PayPalApiException | HttpClientErrorException e) {
            message = "Transaction failed due to error: " + e.getMessage();
        }
        model.put("error", true);
        model.put("pageTitle", pageTitle);
        model.put("message", message);
        return "checkout/checkout_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request,
                                            Customer customer,
                                            Order order) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettingBag();
        CurrencySettingBag currencySettingBag = settingService.getCurrencySettingBag();
        JavaMailSenderImpl mailSender = Utitlity.prepareMailSender(emailSettingBag);

        String customerEmail = customer.getEmail();
        String subject = emailSettingBag.getOrderConfirmSubject();
        String content = emailSettingBag.getOrderConfirmContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(customerEmail);
        helper.setSubject(subject);

        content = content.replace("[[customerName]]", customer.getFullName());
        content = StringUtils.replace(content, "[[orderId]]", String.valueOf(order.getId()));
        content = StringUtils.replace(content, "[[orderTime]]", new SimpleDateFormat("E, dd MMM yyyy").format(order.getOrderTime()));
        content = StringUtils.replace(content, "[[shippingAddress]]", order.getShippingAddress());
        content = StringUtils.replace(content, "[[total]]", Utitlity.formatCurrency(order.getTotal(), currencySettingBag));
        content = StringUtils.replace(content, "[[paymentMethod]]", order.getPaymentMethod().toString());

        String link = Utitlity.getSiteURL(request) + "/orders?id=" + order.getId();
        content = StringUtils.replace(content, "[[orderLink]]", link);

        helper.setText(content, true);
        mailSender.send(message);
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utitlity.getEmailOfAuthenticatedCustomer(request);
        return customerService.findCustomerByEmail(email);
    }
}
