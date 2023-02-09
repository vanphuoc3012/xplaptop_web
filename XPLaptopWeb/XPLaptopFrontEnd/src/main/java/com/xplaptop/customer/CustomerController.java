package com.xplaptop.customer;

import com.xplaptop.Utitlity;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.security.CustomerUserDetailsImpl;
import com.xplaptop.security.oauth2.CustomerOauth2User;
import com.xplaptop.setting.EmailSettingBag;
import com.xplaptop.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(ModelMap model) {
        List<Country> listAllCountries = customerService.listAllCountries();
        model.put("listAllCountries", listAllCountries);
        model.put("pageTitle", "Customer Registration");
        model.put("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer,
                                 ModelMap model,
                                 HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);
        model.put("pageTitle", "Registration Succeed");

        return "/register/register_success";
    }

    @GetMapping("/verify")
    public String verifyCustomer(@RequestParam(name = "code") String code) {
        boolean verified = customerService.verifyCustomer(code);
        if(verified) return "register/verify_succeed";
        return "register/verify_fail";
    }

    @GetMapping("/account_details")
    public String editCustomerInformation(ModelMap model,
                                          Authentication authentication,
                                          HttpServletRequest request) {
        String email = getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.findCustomerByEmail(email);
        List<Country> listAllCountries = customerService.listAllCountries();
        List<State> stateList = customerService.findAllStatesByCountryId(customer.getCountry().getId());
        model.put("listAllCountries", listAllCountries);
        model.put("stateList", stateList);
        model.put("customer", customer);
        return "customer/account_form";
    }

    @PostMapping("/update_account")
    public String saveUpdateAccountInformation(Customer customer,
                                               HttpServletRequest request,
                                               ModelMap model) {
        customerService.updateCustomerInfo(customer);
        updateNameForAuthenticatedCustomer(customer, request);

        boolean error = false;
        model.put("error", error);
        model.put("message", "Your account information has been update");
        model.put("pageTitle", "Successfully update account information");
        return "customer/message";
    }

    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        String fullName = customer.getFullName();
        if(userPrincipal instanceof UsernamePasswordAuthenticationToken ||
                userPrincipal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetailsImpl principal = (CustomerUserDetailsImpl) ((AbstractAuthenticationToken) userPrincipal).getPrincipal();
            principal.getCustomer().setFirstName(customer.getFirstName());
            principal.getCustomer().setLastName(customer.getLastName());
        } else if (userPrincipal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) userPrincipal;
            CustomerOauth2User oAuth2User = (CustomerOauth2User) oAuth2Token.getPrincipal();
            oAuth2User.setFullName(fullName);
        }
    }

    private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        String customerEmail = "";
        if(userPrincipal instanceof UsernamePasswordAuthenticationToken ||
        userPrincipal instanceof RememberMeAuthenticationToken) {
            customerEmail = userPrincipal.getName();
        } else if (userPrincipal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) userPrincipal;
            CustomerOauth2User oAuth2User = (CustomerOauth2User) oAuth2Token.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }
        return customerEmail;
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettingBag();
        JavaMailSenderImpl mailSender = Utitlity.prepareMailSender(emailSettingBag);

        String customerEmail = customer.getEmail();
        String subject = emailSettingBag.getCustomerVerifySubject();
        String content = emailSettingBag.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(customerEmail);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());
        String verifyURL = Utitlity.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }
}
