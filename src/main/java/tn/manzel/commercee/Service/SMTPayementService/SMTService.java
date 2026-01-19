package tn.manzel.commercee.Service.SMTPayementService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;
import tn.manzel.commercee.DAO.Entities.Mysql.PayoutStatus;
import tn.manzel.commercee.DAO.Entities.PostgresSql.Payment;
import tn.manzel.commercee.DAO.Entities.PostgresSql.PaymentStatus;
import tn.manzel.commercee.DAO.Repositories.PostgresSql.PayementRepository;
import tn.manzel.commercee.DTO.PaymentInitRequest;
import tn.manzel.commercee.DTO.PaymentInitResponse;
import tn.manzel.commercee.DTO.SmtWebhookRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SMTService {

    private final PayementRepository paymentRepository;
    private final PayoutService payoutService;

    @Value("${smt.merchant-id}")
    private String merchantId;

    @Value("${smt.secret-key}")
    private String secretKey;

    @Value("${smt.payment-url}")
    private String paymentUrl;

    public String initPayment(PaymentInitRequest request) {

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        String payload = merchantId +
                payment.getId() +
                request.getAmount() +
                request.getCurrency();

        String signature = SMTSignatureService.sign(payload, secretKey);

        return paymentUrl +
                "?merchantId=" + merchantId +
                "&paymentId=" + payment.getId() +
                "&amount=" + request.getAmount() +
                "&currency=" + request.getCurrency() +
                "&acceptUrl=" + URLEncoder.encode(ApiEndpoints.FrontRedirect.PAYMENT_SUCCESS
                                                            , StandardCharsets.UTF_8) +
                "&declineUrl=" + URLEncoder.encode(ApiEndpoints.FrontRedirect.PAYMENT_DECLINE
                                                            , StandardCharsets.UTF_8) +
                "&cancelUrl=" + URLEncoder.encode(ApiEndpoints.FrontRedirect.PAYMENT_CANCEL
                                                            , StandardCharsets.UTF_8) +
                "&signature=" + URLEncoder.encode(signature, StandardCharsets.UTF_8);
    }







    public void handleWebhook(SmtWebhookRequest request) {

        String payload = request.getPaymentId()
                + request.getStatus().toString()
                + request.getAmount()
                + request.getCurrency();

        boolean valid = SMTSignatureService.verify(
                payload,
                request.getSignature(),
                secretKey
        );

        if (!valid) {
            throw new SecurityException("Invalid SMT signature");
        }

        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getAmount().compareTo(request.getAmount()) != 0) {
            payment.setStatus(PaymentStatus.FRAUD_DETECTED);
            paymentRepository.save(payment);
            throw new SecurityException("Montant incohérent ! Tentative de fraude.");
        }

        // 🔒 IDÉMPOTENCE
        if (!payment.getStatus().equals(PaymentStatus.PENDING)) {
            return; // déjà traité
        }

        payment.setStatus(request.getStatus());
        paymentRepository.save(payment);

//        Payout payout = Payout.builder()
//                .sellerId(payment.getOrderId()) // à adapter selon la logique métier
//                .amount(payment.getAmount())
//                .status(PayoutStatus.PENDING)
//                .build();
    }


}
