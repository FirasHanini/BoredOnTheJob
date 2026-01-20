package tn.manzel.commercee.Service.KonnektPaymentService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KonnektPaymentService {
    @Value("${konnekt.api.key}")
    private String apiKey;

    @Value("${konnect.api.url}")
    private String konnektUrl;

    @Value("${konnekt.wallet.id}")
    private String walletId;


    private final String initPayment="payments/init-payment";

    public Map<String, Object> initierPaiement(Double montant, String description) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("receiverWalletId", walletId);
        body.put("amount", (int)(montant * 1000)); // En millimes pour TND
        body.put("token", "TND");
        body.put("description", description);
        body.put("successUrl", "http://localhost:4200/products");
        body.put("failUrl", "http://localhost:4200/products");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(konnektUrl+initPayment, request, Map.class);
    }
}
