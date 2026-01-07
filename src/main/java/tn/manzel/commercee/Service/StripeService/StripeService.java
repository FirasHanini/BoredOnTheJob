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


        long commission = (long)(product.getPrice()) / 10 ; // 10%

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        // --- No redirect URL pour test sur postman ---
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                        .build()
                        )
                        // --------------------------------------------------------------
                        .setAmount((long) (product.getPrice() ))
                        .setCurrency("eur")
                        .setDescription(product.getName())
                        .setApplicationFeeAmount(commission)
                        .setTransferData(
                                PaymentIntentCreateParams.TransferData.builder()
                                        .setDestination(product.getSeller().getStripeAccountId())
                                        .build()
                        )

                        .build();

        return PaymentIntent.create(params);
    }

}
