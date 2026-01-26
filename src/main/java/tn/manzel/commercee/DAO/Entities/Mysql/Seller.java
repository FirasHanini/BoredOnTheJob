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

    // To be removed
    private String stripeAccountId;
    @NonNull
    private String rib;
}
