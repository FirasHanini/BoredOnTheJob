package tn.manzel.commercee.DAO.Repositories.PostgresSql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.manzel.commercee.DAO.Entities.PostgresSql.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
