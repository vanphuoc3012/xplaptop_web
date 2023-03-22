package com.xplaptop.email;

import com.xplaptop.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Value("${amqp.exchange.email}")
    private String emailExchangeTopic;
    public void sendEmail(EmailDTO emailDTO) {
        log.info("Publishing email event, sending email to: {}", emailDTO.getToEmail());
        String routingKey = "email";
        amqpTemplate.convertAndSend(emailExchangeTopic, routingKey, emailDTO);
    }
}
