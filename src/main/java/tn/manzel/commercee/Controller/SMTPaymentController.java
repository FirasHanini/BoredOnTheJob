package tn.manzel.commercee.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;
import tn.manzel.commercee.DTO.PaymentInitRequest;
import tn.manzel.commercee.DTO.PaymentInitResponse;
import tn.manzel.commercee.DTO.SmtWebhookRequest;
import tn.manzel.commercee.Service.AuditService.Auditable;
import tn.manzel.commercee.Service.SMTPayementService.SMTService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.SMTPAYEMENT_BASE)
public class SMTPaymentController
{

    private final SMTService smtService;


    @Auditable(action = AuditAction.CREATE, entity = "SMT Payment Init")
    @PostMapping(ApiEndpoints.SMTEndpoints.INIT_PAYMENT)
    public ResponseEntity<PaymentInitResponse> init(@RequestBody @Valid PaymentInitRequest request) {
        String redirectUrl = smtService.initPayment(request);
        return ResponseEntity.ok(
                PaymentInitResponse.builder()
                        .redirectUrl(redirectUrl)
                        .build()
        );


    }


    @Auditable(action = AuditAction.CREATE, entity = "SMT Payment Callback")
    @PostMapping(ApiEndpoints.SMTEndpoints.CALLBACK_PAYMENT)
    public ResponseEntity<Void> callback(@RequestBody @Valid SmtWebhookRequest request) {
        smtService.handleWebhook(request);
        return ResponseEntity.ok().build();
    }
}
