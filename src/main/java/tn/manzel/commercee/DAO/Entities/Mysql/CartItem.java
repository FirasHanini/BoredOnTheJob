package tn.manzel.commercee.DAO.Entities.Mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JsonIgnore
    private User buyer;

    private int quantity;



    @Override
    public String toString() {
        return this.getQuantity()+" *"+this.getProduct().getName();
    }
}
