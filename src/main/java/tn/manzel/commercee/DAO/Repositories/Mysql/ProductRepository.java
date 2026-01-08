package tn.manzel.commercee.DAO.Repositories.Mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByIdAndSellerId(Long productId, Long sellerId);
}
