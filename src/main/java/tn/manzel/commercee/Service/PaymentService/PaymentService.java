package tn.manzel.commercee.Service.PaymentService;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.Service.CartService.CartService;
import tn.manzel.commercee.Service.KonnektPaymentService.KonnektPaymentService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final CartService cartService;
    private final PaymentService paymentService;


    public long getTotalAmount(String email) {
        return cartService.calculateTotalPrice(email);
    }


    @Transactional
    public Map<String, Object> payCart(String email){
        long totalAmount = cartService.calculateTotalPrice(email);
        List<CartItem> items= cartService.getCart(email);
        String description= "Payment from "+email+" for "+items.toString();

        // add Payouts, pending, success, after webhook payement success, so the weekly selleres paiement is done
        return paymentService.initierPaiement((double)totalAmount, description) ;
    }




    public long getTotalAmountPerSeller(String email)
    {
        return 0;
    }



}
