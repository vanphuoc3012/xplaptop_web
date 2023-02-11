package com.xplaptop;

import com.xplaptop.security.oauth2.CustomerOauth2User;
import com.xplaptop.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Properties;

public class Utitlity {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(),"");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag emailSettingBag) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSettingBag.getHost());
        mailSender.setPort(emailSettingBag.getPort());
        mailSender.setUsername(emailSettingBag.getUserName());
        mailSender.setPassword(emailSettingBag.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.starttls.enable", emailSettingBag.getSmtpSecured());
        mailProperties.setProperty("mail.smtp.auth", emailSettingBag.getSmtpAuth());


        mailSender.setJavaMailProperties(mailProperties);
        return mailSender;
    }

    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        if(userPrincipal == null) return null;
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
}
