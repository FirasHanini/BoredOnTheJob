package tn.manzel.commercee.Controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;
import tn.manzel.commercee.Service.AuditService.Auditable;
import tn.manzel.commercee.Service.CartService.CartService;
import tn.manzel.commercee.Service.ProductService.ProductService;
import tn.manzel.commercee.Service.StripeService.StripeService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class StripeController {
    private final StripeService stripeService;
    private final ProductService productService;


    @Value("${stripe.webhook.secret}")
    private String endpointSecret;



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

        // 2. Traitement si le paiement est réussi
        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();

            // a. Récupérer l'ID produit stocké dans les métadonnées lors de la création
            Long productId = Long.parseLong(intent.getMetadata().get("productId"));

            try {
                // b. Déclencher le transfert vers le vendeur
                stripeService.transferToSeller(productId);

//                // c. (Optionnel) Marquer la commande comme "Payée" en base de données
//                orderService.confirmPayment(productId);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(e.getMessage());
                        //esponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok().build();
    }

















    }
