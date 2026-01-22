package tn.manzel.commercee.DTO;

import lombok.Data;

import java.math.BigDecimal;


public record PayoutSummaryDTO(String sellerEmail,
                               BigDecimal totalAmount,
                               String sellerRib) {
}
