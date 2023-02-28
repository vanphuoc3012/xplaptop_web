package com.xplaptop.test.checkout.paypal;

import com.xplaptop.checkout.paypal.PayPalOrderResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class PayPalApiTests {
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AQAcoT69BViHE6RcSZQpWeUg-jCyV7sMpQafewsAmjkLILN-cMZcV7OC-KaMxk_uGDz1d1oydapQEcEh";
    private static final String CLIENT_SECRET = "ECaw7VWSxACW2nwBvLElswQkDPh1tos-i6j-LmrUZomLoflKRKcVB829VRN0FAGO7gCQ_nj8_TKxI0Xy";

    @Test
    public void testGetOrderDetails() {
        String orderId = "0S290076CP896044E";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en-US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, PayPalOrderResponse.class);
        PayPalOrderResponse body = response.getBody();
        Assertions.assertThat(body.validate(orderId)).isTrue();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

    }
}
