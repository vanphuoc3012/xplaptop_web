package com.xplaptop.customer;

import com.xplaptop.Utitlity;
import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.setting.EmailSettingBag;
import com.xplaptop.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
    public String verifyCustomer(@RequestParam(name = "code") String code,
                                 ModelMap model) {
        boolean verified = customerService.verifyCustomer(code);

        if(verified) return "register/verified";
        return "register/verified_fail";
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

        System.out.println("To address: " + customerEmail);
        System.out.println("Verify URL: "+verifyURL);
    }
}
