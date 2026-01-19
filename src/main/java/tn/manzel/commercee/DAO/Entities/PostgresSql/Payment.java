package tn.manzel.commercee.DAO.Entities.PostgresSql;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private Long orderId;
    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String provider; // SMT / CLICK_TO_PAY

    private String transactionId;

    private LocalDateTime createdAt;
}
