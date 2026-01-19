package tn.manzel.commercee.DAO.Repositories.PostgresSql;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.manzel.commercee.DAO.Entities.PostgresSql.Payment;

public interface PayementRepository extends JpaRepository<Payment,Long> {
}
