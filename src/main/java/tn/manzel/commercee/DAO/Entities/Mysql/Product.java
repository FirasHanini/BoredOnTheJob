package tn.manzel.commercee.DAO.Entities.Mysql;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@SQLRestriction("deleted = false")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private double price; // en dinars (ou euros)

    private int stock;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
