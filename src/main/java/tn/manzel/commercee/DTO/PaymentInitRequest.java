package tn.manzel.commercee.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentInitRequest {

    @NotNull
    private Long orderId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Size(min = 3, max = 3)
    private String currency; // TND

    @NotNull
    private String returnUrl;

    @NotNull
    private String cancelUrl;
}
