package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DTO.PaymeeWebhookDTO;
import tn.manzel.commercee.Service.PaymeeService.PaymeeService;
import tn.manzel.commercee.Service.PaymentService.PaymentService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping(ApiEndpoints.PAYMEE_BASE)
@RequiredArgsConstructor
@Slf4j
public class PaymeeController {
    private final PaymeeService paymeeService;
    private final PaymentService paymentCalculService;


//    @PostMapping(ApiEndpoints.paymeetEndpoints.CREATE)
//    public ResponseEntity<Object> createPayment(@RequestParam double amount,
//                                                             @RequestParam String description,
//                                                             Authentication auth) {
//        String email = auth.getName();
//        return ResponseEntity.ok().body(paymeeService.createPayment(amount, description, email));
//    }


    @PostMapping(ApiEndpoints.paymeetEndpoints.WEBHOOK)
    public ResponseEntity<String> handlePaymeeWebhook(PaymeeWebhookDTO payload) {
        // 1. Extraire le token de paiement envoyé par Paymee;
        if ( payload.getToken() == null) {
            return ResponseEntity.badRequest().body("Token missing");
        }
        log.info("PaymeeWebhook payment status: " + payload.isPayment_status()) ;
        // 2. Vérifier le statut du paiement
        if (!payload.isPayment_status())
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Échec du paiement");

        // 3. Vérifier le montant reçu
        long expectedAmount = paymentCalculService.getTotalAmount(payload.getEmail());
        if(payload.getAmount()!=expectedAmount){
            log.error("Montant reçu (" + payload.getAmount() + ") ne correspond pas au montant attendu (" + expectedAmount + ")");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount Mismatch");
        }

        paymentCalculService.postWebhookActions(
                payload.getEmail()
        );

         log.info("Paiement confirmé");
        return ResponseEntity.ok("Payment Validated Successfully");

    }



    @GetMapping(ApiEndpoints.paymeetEndpoints.EXPORT_PAYMENT)
    public ResponseEntity<Resource> exportCsv() throws IOException {
        // Générer le contenu
        String csvContent = paymentCalculService.getCsvToPaySellers();

        String folderPath = "C:/Users/hanin/OneDrive/Bureau/Front";
        Path path = Paths.get(folderPath).resolve("payouts.csv");

        // 2. Écrire le fichier physiquement sur le disque
        Files.write(path, csvContent.getBytes(StandardCharsets.UTF_8));
        byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payouts.csv")
                .contentType(MediaType.parseMediaType("text/csv"))

                .body(resource);
    }



    @PostMapping()
    public ResponseEntity<Map<String, Object>> createPayment(Authentication auth){
        return ResponseEntity.ok().body(paymentCalculService.payCart(auth.getName()));
    }
}
