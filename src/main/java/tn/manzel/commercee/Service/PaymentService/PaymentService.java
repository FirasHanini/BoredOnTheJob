package tn.manzel.commercee.Service.PaymentService;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;
import tn.manzel.commercee.DAO.Entities.Mysql.PayoutStatus;
import tn.manzel.commercee.DTO.PayoutSummaryDTO;
import tn.manzel.commercee.Service.CartService.CartService;

import tn.manzel.commercee.Service.PaymeeService.PaymeeService;
import tn.manzel.commercee.Service.PayoutService.PayoutService;
import tn.manzel.commercee.Service.ProductService.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final CartService cartService;
    private final PaymeeService paymentService;
    private final PayoutService payoutService;
    private final ProductService productService;


    public long getTotalAmount(String email) {
        return cartService.calculateTotalPrice(email);
    }


    @Transactional
    public Map<String, Object> payCart(String email){
        long totalAmount = cartService.calculateTotalPrice(email);
        List<CartItem> items= cartService.getCart(email);
        String description= "Payment from "+email+" for "+items.toString();

        // add Payouts, pending, success, after webhook payement success, so the weekly sellers paiement is done
        return paymentService.createPayment((double)totalAmount,description,email);

    }



    public BigDecimal amountAfterCommission(BigDecimal amount)
    {
        double commissionRate = 0.05; // 5% commission

       return amount.multiply(
                BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(commissionRate)
                )
        );

    }


    public void postWebhookActions(String email)
    {
        List<CartItem>items= cartService.getCart(email);
        for(CartItem item: items) {
            double amount = item.getProduct().getPrice() * item.getQuantity();
            Payout payout = Payout.builder()
                    .generatedAt(LocalDateTime.now())
                    .amount(amountAfterCommission(BigDecimal.valueOf(amount)))
                    .status(PayoutStatus.PENDING)
                    .itemsSold(item.toString())
                    .seller(item.getProduct().getSeller())
                    .paidAt(null)
                    .build();

         payoutService.savePayout(payout);

         productService.updateStock(item.getProduct().getId(), item.getQuantity());

        }
        cartService.flushCart(email);

        // create Payout for sellers
        // update product stock
        // flush cart

    }



    //Paying Sellers

    public String getCsvToPaySellers()
    {
        List<PayoutSummaryDTO> payouts= payoutService.getPendingGroupedByseller();

   //en-tête du fichier
        StringBuilder csv = new StringBuilder();
        csv.append("Nom du beneficiaire" +
                ";RIB" +
                ";Montant" +
                ";Motif\n");


        for (PayoutSummaryDTO dto : payouts) {
            csv.append(dto.sellerEmail()).append(";")
                    .append(dto.sellerRib()).append(";")
                    .append(dto.totalAmount()).append(";")
                    .append("Payement de la plateform de vente").append("\n");

        }

        return csv.toString();
    }


}
