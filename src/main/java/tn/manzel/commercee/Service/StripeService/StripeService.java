package tn.manzel.commercee.Service.StripeService;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import com.stripe.param.TransferCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import com.stripe.param.PaymentIntentCreateParams;
import tn.manzel.commercee.Service.CartService.CartService;

import java.util.List;

@Slf4j
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
    public PaymentIntent createPaymentIntent(String email) throws Exception {
        long amount = cartService.calculateTotalPrice(email);


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
          //     .putMetadata("itemId", String.valueOf(item.getId()))
                .putMetadata("userEmail", String.valueOf(email))
                .build();

        return PaymentIntent.create(params);
    }

    public void transferToSeller(Long cartItemId, String chargeId) throws Exception {
        try {
            log.info("transferToSeller/////////////////////////////////////////////////////////////////////");
            CartItem cartItem = cartService.findById(cartItemId);
            long totalAmount = (long) cartItem.getProduct().getPrice() * cartItem.getQuantity();
            long netAmount = totalAmount - (totalAmount / 10); // On retire 10% de commission

            TransferCreateParams params = TransferCreateParams.builder()
                    // Source transaction charge ID
                    .setSourceTransaction(chargeId)
                    .setAmount(netAmount)
                    .setCurrency("eur")
                    .setDestination(cartItem.getProduct().getSeller().getStripeAccountId())
                    .setDescription("Vente du produit : " + cartItem.getProduct().getName())
                    .build();


            Transfer transfer = Transfer.create(params);
            log.info("TRANSFERT RÉUSSI ! ID: " + transfer.getId() + " / Destinataire: " + transfer.getDestination());
        } catch (StripeException e) {
            // C'EST ICI que vous verrez pourquoi le debug s'arrête !
            log.error("ERREUR STRIPE : " + e.getMessage());
            log.error("Code d'erreur : " + e.getCode());
        } catch (Exception e) {
            log.error("ERREUR GÉNÉRALE : " + e.getMessage());
        }

    }


    public void paySerllers(String email, String chargeId) throws Exception {

        List<CartItem> cartItems = cartService.getCart(email);
        log.info("Cart items "+cartItems.toString());
        for (CartItem item : cartItems) {
            transferToSeller(item.getId(),chargeId);
        }
//        cartService.clearCartByUser(cartItems.get(0).getUser());
    }













}
