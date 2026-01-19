package tn.manzel.commercee.DAO.Repositories.Mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;

import java.util.List;

public interface PayoutRepository  extends JpaRepository<Payout, Long> {

    @Query("SELECT p FROM Payout p JOIN FETCH p.seller where p.status='PENDING'")
    List<Payout> findPendingWithSeller();
}
