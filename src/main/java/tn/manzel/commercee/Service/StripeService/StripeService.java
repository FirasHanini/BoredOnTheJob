package tn.manzel.commercee.Service.StripeService;

import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.PostgresSql.Product;
import com.stripe.param.PaymentIntentCreateParams;

@Service
@RequiredArgsConstructor
public class StripeService {
    public PaymentIntent createPayment(Product product) throws Exception {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) (product.getPrice() )) // cents
                        .setCurrency("eur")
                        .setDescription(product.getName())
                        // --- AJOUTE CE BLOC ICI ---
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                        .build()
                        )
                        // --------------------------
                        .build();

        return PaymentIntent.create(params);
    }

}
