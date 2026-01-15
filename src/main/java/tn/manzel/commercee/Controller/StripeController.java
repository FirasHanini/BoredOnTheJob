package tn.manzel.commercee.Controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;
import tn.manzel.commercee.Service.AuditService.Auditable;

import tn.manzel.commercee.Service.ProductService.ProductService;
import tn.manzel.commercee.Service.StripeService.StripeService;


import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class StripeController {
    private final StripeService stripeService;
    private final ProductService productService;


    @Value("${stripe.webhook.secret}")
    private String endpointSecret;



//    @Auditable(action = AuditAction.CREATE, entity = "N/A - Payment Intent Cart Pay")
    @PostMapping()
    public ResponseEntity<Map<String, String>> PayCart(Authentication auth) throws Exception {
        String email = auth.getName();
        var intent= stripeService.createPaymentIntent(email);
        return ResponseEntity.ok().body(Map.of(
                "clientSecret", intent.getClientSecret())
        );
    }



    @Auditable(action = AuditAction.CREATE, entity = "N/A - Payment Intent")
    @PostMapping("/create/{productId}")
    public ResponseEntity<Map<String, String>> createPaymentFromProduct(@PathVariable Long productId) throws Exception {

        Product product = productService.findById(productId);

        if(product == null){
            return ResponseEntity.badRequest().build();
        }

        var intent = stripeService.createPayment(product);

        return ResponseEntity.ok().body(Map.of(
                "clientSecret", intent.getClientSecret())
        );
    }



    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            // 1. Vérification que la requête vient bien de Stripe
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Bad Request");

        }

        String eventType = event.getType();

        // 2. Traitement si le paiement est réussi
        if ("payment_intent.succeeded".equals(event.getType())) {

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            PaymentIntent intent = null;

            if (dataObjectDeserializer.getObject().isPresent()) {
                intent = (PaymentIntent) dataObjectDeserializer.getObject().get();
            } else {
                // Si la désérialisation directe échoue, on force le mapping manuel (très utile en cas de conflit de version)
                intent = (PaymentIntent) event.getData().getObject();
            }

            if (intent == null) {
                log.error("Impossible de récupérer le PaymentIntent depuis l'événement Stripe");
                return ResponseEntity.badRequest().body("Payload désérialisation failed");
            }




            // a. Récupérer l'ID produit stocké dans les métadonnées lors de la création
            //Long itemId = Long.parseLong(intent.getMetadata().get("itemId"));
            String userEmail = intent.getMetadata().get("userEmail");

            String chargeId = intent.getLatestCharge();

            try {
                // b. Déclencher le transfert vers le vendeur

                stripeService.paySerllers(userEmail, chargeId);

//                // c. (Optionnel) Marquer la commande comme "Payée" en base de données
//                orderService.confirmPayment(itemId);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(e.getMessage());
                        //esponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok().build();
    }




















    }
