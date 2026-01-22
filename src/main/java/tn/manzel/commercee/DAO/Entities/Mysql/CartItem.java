package tn.manzel.commercee.DAO.Entities.Mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JsonIgnore
    private User user;

    private int quantity;

    @Override
    public String toString() {
        return "CartItem{" + quantity+
                " x product=" + product +
                '}';
    }
}
