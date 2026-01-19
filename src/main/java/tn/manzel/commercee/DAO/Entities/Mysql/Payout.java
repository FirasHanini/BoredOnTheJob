package tn.manzel.commercee.DAO.Entities.Mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Payout {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private Seller seller;

    private BigDecimal amount;
    private LocalDateTime generatedAt;
    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    private PayoutStatus status; // PENDING, COMPLETED
}
