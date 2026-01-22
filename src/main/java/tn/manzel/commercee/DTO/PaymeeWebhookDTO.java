package tn.manzel.commercee.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymeeWebhookDTO {

    private String token;

    @JsonProperty("check_sum")
    private String check_sum;

    @JsonProperty("payment_status")
    private boolean payment_status;

    @JsonProperty("order_id")
    private String order_id;


    private String first_name;


    private String last_name;

    private String email;
    private String phone;
    private String note;
    private Double amount;


    private Long transactionId;


    private Double received_amount;

    private Double cost;

//    public boolean isPaid() {
//        // Gère les variantes de casse "True", "true", "TRUE"
//        return "true".equalsIgnoreCase(paymentStatus);
//    }
}
