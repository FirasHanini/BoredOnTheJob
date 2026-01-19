package tn.manzel.commercee.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PaymentInitResponse {

    private String redirectUrl;
}
