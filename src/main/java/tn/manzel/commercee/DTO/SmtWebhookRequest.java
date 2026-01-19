package tn.manzel.commercee.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.manzel.commercee.DAO.Entities.PostgresSql.PaymentStatus;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SmtWebhookRequest {

    @NotNull
    private Long paymentId;

    @NotNull
    private PaymentStatus status; // SUCCESS | FAILED

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currency;

    @NotNull
    private String signature;

}
