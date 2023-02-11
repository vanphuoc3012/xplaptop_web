package com.xplaptop.customer;

import com.xplaptop.Utitlity;
import com.xplaptop.common.exception.CustomerNotFoundException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/forgot_password")
    public String viewForgotPasswordRequestForm(ModelMap model) {
        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordRequest(HttpServletRequest request,
                                               RedirectAttributes ra) {
        String email = request.getParameter("email");
        boolean error = true;
        try {
            String token = customerService.updateResetPasswordToken(email);
            String resetPasswordLink = Utitlity.getSiteURL(request) + "/reset_password?token="+token;
            sendResetEmail(resetPasswordLink, email);
            error = false;
            ra.addFlashAttribute("message", "We have sent you an email to reset your password");
        } catch (MessagingException | UnsupportedEncodingException | CustomerNotFoundException e) {
            ra.addFlashAttribute("message", "Invalid email");
        }
        ra.addFlashAttribute("error", error);
        return "redirect:/forgot_password";
    }

    @GetMapping("/reset_password")
    public String showFormResetPassword(@RequestParam(name = "token") String token,
                                        ModelMap model) {
        model.put("token", token);
        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processNewPassword(HttpServletRequest request,
                                     ModelMap model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        try {
            customerService.updateNewPassword(token, password);
            model.put("error", false);
            model.put("message", "You have changed your password successfully");
        } catch (CustomerNotFoundException e) {
            model.put("error", true);
            model.put("message", "Invalid token");
        }
        return "customer/message";
    }

    private void sendResetEmail(String url, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettingBag();
        JavaMailSenderImpl mailSender = Utitlity.prepareMailSender(emailSettingBag);
        String subject = "Reset password";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(email);
        helper.setSubject(subject);

        String content = "\n" +
                "<!doctype html>\n" +
                "<html lang=\"en-US\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />\n" +
                "    <title>Reset Password Email Template</title>\n" +
                "    <meta name=\"description\" content=\"Reset Password Email Template.\">\n" +
                "    <style type=\"text/css\">\n" +
                "        a:hover {text-decoration: underline !important;}\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body marginheight=\"0\" topmargin=\"0\" marginwidth=\"0\" style=\"margin: 0px; background-color: #f2f3f8;\" leftmargin=\"0\">\n" +
                "    <!--100% body table-->\n" +
                "    <table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#f2f3f8\"\n" +
                "        style=\"@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;\">\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <table style=\"background-color: #f2f3f8; max-width:670px;  margin:0 auto;\" width=\"100%\" border=\"0\"\n" +
                "                    align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                    <tr>\n" +
                "                        <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"text-align:center;\">\n" +
                "                          <a href=\"#\" title=\"logo\" target=\"_blank\">\n" +
                "                            <img src=\"https://skillicons.dev/icons?i=java,spring" +
                "\" title=\"logo\" alt=\"logo\">\n" +
                "                          </a>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <table width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "                                style=\"max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);\">\n" +
                "                                <tr>\n" +
                "                                    <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding:0 35px;\">\n" +
                "                                        <h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;\">You have\n" +
                "                                            requested to reset your password</h1>\n" +
                "                                        <span\n" +
                "                                            style=\"display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;\"></span>\n" +
                "                                        <p style=\"color:#455056; font-size:15px;line-height:24px; margin:0;\">\n" +
                "                                            We cannot simply send you your old password. A unique link to reset your\n" +
                "                                            password has been generated for you. To reset your password, click the\n" +
                "                                            following link and follow the instructions.\n" +
                "                                        </p>\n" +
                "                                        <a href=\"[[URL]]\"\n" +
                "                                            style=\"background:#20e277;text-decoration:none !important; font-weight:500; margin-top:35px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;\">Reset\n" +
                "                                            Password</a>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    <tr>\n" +
                "                        <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                 \n" +
                "                    <tr>\n" +
                "                        <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "    <!--/100% body table-->\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        content = content.replace("[[URL]]", url);
        helper.setText(content, true);

        mailSender.send(message);
    }


}
