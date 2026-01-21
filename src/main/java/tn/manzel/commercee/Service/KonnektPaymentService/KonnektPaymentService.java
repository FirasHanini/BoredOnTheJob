package tn.manzel.commercee.Service.KonnektPaymentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KonnektPaymentService {
    @Value("${konnekt.api.key}")
    private String apiKey;

    @Value("${konnect.api.url}")
    private String konnektUrl;

    @Value("${konnekt.wallet.id}")
    private String walletId;


    private final String PAYMENTS = "payments/";
    private final String INITPAYMENTS =PAYMENTS+"init-payment";

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
        body.put("silentWebhook", "true");
        body.put("webhook", ApiEndpoints.NGROK+ ApiEndpoints.KONNEKT_BASE
                +ApiEndpoints.konnektEndpoints.WEBHOOK_PAYMENT);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(konnektUrl+ INITPAYMENTS, request, Map.class);
    }


    public String verifyPaymentStatus(String paymentRef) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("x-api-key", apiKey);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            // Appel à l'API Konnect
//            ResponseEntity<Map> response = restTemplate.exchange(konnektUrl+PAYMENTS+paymentRef, HttpMethod.GET, entity, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                Map<String, Object> paymentDetails = (Map<String, Object>) response.getBody().get("payment");
//                String status = (String) paymentDetails.get("status");
//
//                // On vérifie si le statut est bien "completed"
//                return status;
//            }
//        } catch (Exception e) {
//            System.err.println("Erreur lors de la vérification Konnect: " + e.getMessage());
//        }
//        return "false";

        if(getPaymentDetails(paymentRef)==null)
            return null;
        return getPaymentDetails(paymentRef).get("status").toString();
    }


    public Map<String,Object> getPaymentDetails(String paymentRef) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Appel à l'API Konnect
            ResponseEntity<Map> response = restTemplate.exchange(konnektUrl+PAYMENTS+paymentRef, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> paymentDetails = (Map<String, Object>) response.getBody().get("payment");

                log.info("paymentDetails: "+paymentDetails);
                return paymentDetails;
            }
        } catch (Exception e) {
            log.error("Erreur lors de la vérification Konnect: " + e.getMessage());

        }

        return null;
    }
}
