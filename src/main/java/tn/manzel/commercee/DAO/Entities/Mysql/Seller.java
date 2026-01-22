package tn.manzel.commercee.DAO.Entities.Mysql;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "seller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Seller extends User {

    private String stripeAccountId;
    @NonNull
    private String iban;
}
