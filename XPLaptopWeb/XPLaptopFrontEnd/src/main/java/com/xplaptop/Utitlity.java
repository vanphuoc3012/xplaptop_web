package com.xplaptop;

import com.xplaptop.security.oauth2.CustomerOauth2User;
import com.xplaptop.setting.CurrencySettingBag;
import com.xplaptop.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    public static String formatCurrency(double amount, CurrencySettingBag currencySettingBag) {
        String symbol = currencySettingBag.getCurrencySymbol();
        String position = currencySettingBag.getCurrencySymbolPosition();
        String decimalPointType = currencySettingBag.getDecimalPointType();
        String thousandsPointType = currencySettingBag.getThousandsPointType();
        int currencyDigits = currencySettingBag.getCurrencyDigits();

        StringBuilder sb = new StringBuilder("Before".equals(position) ? symbol : "");
        sb.append("###,###");
        if(currencyDigits > 0) {
            sb.append(".");
            sb.append("#".repeat(currencyDigits));
        }
        sb.append("After".equals(position) ? " " + symbol : "");

        char decimalPointSeparator = "POINT".equals(decimalPointType) ? '.' : ',';
        char thousandsPointSeparator = "POINT".equals(thousandsPointType) ? '.' : ',';
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalPointSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandsPointSeparator);

        DecimalFormat formatter = new DecimalFormat(sb.toString(), decimalFormatSymbols);
        return formatter.format(amount);
    }

    public static String formatCurrencyForPayPal(double amount, CurrencySettingBag currencySettingBag) {
        String decimalPointType = currencySettingBag.getDecimalPointType();
        String thousandsPointType = currencySettingBag.getThousandsPointType();
        int currencyDigits = currencySettingBag.getCurrencyDigits();

        StringBuilder sb = new StringBuilder();
        sb.append("###,###");
        if(currencyDigits > 0) {
            sb.append(".");
            sb.append("#".repeat(currencyDigits));
        }
        char decimalPointSeparator = "POINT".equals(decimalPointType) ? '.' : ',';
        char thousandsPointSeparator = "POINT".equals(thousandsPointType) ? '.' : ',';
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalPointSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandsPointSeparator);

        DecimalFormat formatter = new DecimalFormat(sb.toString(), decimalFormatSymbols);
        return formatter.format(amount);
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
