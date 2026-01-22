package tn.manzel.commercee.Service.PaymeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymeeService {

    @Value("${paymee.acount.number}")
    private  String vendorId;

    @Value("${paymee.acount.token}")
    private String token;

    @Value("${paymee.payment.url}")
    private String paymeeURL;


    public Map<String, Object> createPayment(Double amount, String description, String email) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("vendor", vendorId);
        body.put("amount", amount);
        body.put("note", "Paiement Description " + description);
        body.put("first_name", "Acheteur");
        body.put("last_name", "Test");
        body.put("email", email);
        body.put("phone", "21600000000");
        body.put("return_url", "https://localhost:4200/products");
        body.put("cancel_url", "https://localhost:4200/payment-fail");
        body.put("webhook_url", ApiEndpoints.NGROK
                                +ApiEndpoints.PAYMEE_BASE
                                +ApiEndpoints.paymeetEndpoints.WEBHOOK);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // La réponse contient "data" -> "payment_url" et "token"
        ResponseEntity<Map> response = restTemplate.postForEntity(paymeeURL, request, Map.class);
        return (Map<String, Object>)  response.getBody();


        //
    }
}
