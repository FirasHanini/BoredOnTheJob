package tn.manzel.commercee.DAO.Repositories.Mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;
import tn.manzel.commercee.DAO.Entities.Mysql.PayoutStatus;
import tn.manzel.commercee.DTO.PayoutSummaryDTO;

import java.util.List;

public interface PayoutRepository extends JpaRepository<Payout,Long> {



    @Query("SELECT new tn.manzel.commercee.DTO.PayoutSummaryDTO(s.firstName, SUM(p.amount), s.iban) " +
            "FROM Payout p " +
            "JOIN p.seller s " +
            "WHERE p.status = :status " +
            "GROUP BY s.id, s.email, s.iban")
    List<PayoutSummaryDTO> getPayoutsByStatusGroupedBySellers(@Param("status") PayoutStatus status);

    List<Payout> findAllByStatus(PayoutStatus status);
}
