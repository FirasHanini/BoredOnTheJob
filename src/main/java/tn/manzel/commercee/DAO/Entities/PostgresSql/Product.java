package tn.manzel.commercee.DAO.Entities.PostgresSql;

import jakarta.persistence.*;
import lombok.*;
import tn.manzel.commercee.DAO.Entities.Mysql.Seller;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

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
