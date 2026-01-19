package tn.manzel.commercee.DAO.Repositories.Mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.manzel.commercee.DAO.Entities.Mysql.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
}
