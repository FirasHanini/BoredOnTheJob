package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.DAO.Entities.PostgresSql.Product;
import tn.manzel.commercee.Service.ProductService.ProductService;
import tn.manzel.commercee.Service.StripeService.StripeService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class StripeController {
    private final StripeService stripeService;
    private final ProductService productService;

    @PostMapping("/create/{productId}")
    public ResponseEntity<Map<String, String>> createPayment(@PathVariable Long productId) throws Exception {

        Product product = productService.findById(productId);

        if(product == null){
            return ResponseEntity.badRequest().build();
        }

        var intent = stripeService.createPayment(product);

        return ResponseEntity.ok().body(Map.of(
                "clientSecret", intent.getClientSecret())
        );
    }
}
