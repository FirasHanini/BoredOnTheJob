package tn.manzel.commercee.DAO.Repositories.Mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.manzel.commercee.DAO.Entities.Mysql.CartItem;
import tn.manzel.commercee.DAO.Entities.Mysql.User;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByBuyerEmail(String email);
    Optional<CartItem> findByBuyerEmailAndProductId(String email, Long productId);
    void deleteByBuyer(User user);
}
