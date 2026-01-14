package tn.manzel.commercee.Service.StripeService;

import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import com.stripe.param.TransferCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import com.stripe.param.PaymentIntentCreateParams;
import tn.manzel.commercee.Service.CartService.CartService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StripeService {
    private final CartService cartService;

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


    // Cart Payment Intent paying the full amount to our platform account
    public PaymentIntent createPaymentIntent(CartItem item, Long userId) throws Exception {
        long amount = (long) (item.getProduct().getPrice())* item.getQuantity(); // Stripe utilise les centimes (ex: 1000 pour 10€)

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("eur")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                // IMPORTANT : On stocke les infos dans les métadonnées pour le Webhook
                .putMetadata("itemId", String.valueOf(item.getId()))
                .putMetadata("userId", String.valueOf(userId))
                .build();

        return PaymentIntent.create(params);
    }

    public void transferToSeller(Long cartItemId) throws Exception {
        CartItem cartItem = cartService.findById(cartItemId);
        long totalAmount = (long) cartItem.getProduct().getPrice() * cartItem.getQuantity();
        long netAmount = totalAmount - (totalAmount / 10); // On retire 10% de commission

        TransferCreateParams params = TransferCreateParams.builder()
                .setAmount(netAmount)
                .setCurrency("eur")
                .setDestination(cartItem.getProduct().getSeller().getStripeAccountId())
                .setDescription("Vente du produit : " + cartItem.getProduct().getName())
                .build();

        Transfer.create(params);
    }













}
