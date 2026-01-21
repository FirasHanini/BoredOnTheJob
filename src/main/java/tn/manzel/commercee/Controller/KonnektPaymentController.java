package tn.manzel.commercee.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.Service.KonnektPaymentService.KonnektPaymentService;

import java.util.Map;

@RestController
@RequestMapping(ApiEndpoints.KONNEKT_BASE)
@RequiredArgsConstructor
public class KonnektPaymentController {

    private final KonnektPaymentService konnektPaymentService;


    @PostMapping(ApiEndpoints.konnektEndpoints.INIT_PAYMENT)
    public Map<String, Object> initierPaiement(@RequestParam Double montant, Authentication auth) {
        String description="Payment From"+auth.getName();
        return konnektPaymentService.initierPaiement(montant, description);
    }



    @GetMapping(ApiEndpoints.konnektEndpoints.WEBHOOK_PAYMENT)
    public ResponseEntity<String> handleWebhook(@RequestParam("payment_ref") String paymentRef) {
        System.out.println("Webhook reçu pour la référence : " + paymentRef);

        // Étape 2 : On demande à notre service de vérifier le statut auprès de Konnect
        String success = konnektPaymentService.verifyPaymentStatus(paymentRef);

//        if (success) {
//            // C'est ici que vous déclenchez le virement vers le RIB du vendeur
//            return ResponseEntity.ok("Paiement validé et traité");
//        } else {
//            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Paiement non complété");
//        }

        if(success==null)
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Paiement non complété");

        return ResponseEntity.ok("Paiement validé et traité");

    }
}
