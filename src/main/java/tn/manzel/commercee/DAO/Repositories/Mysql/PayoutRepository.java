package tn.manzel.commercee.DAO.Repositories.Mysql;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;
import tn.manzel.commercee.DAO.Entities.Mysql.PayoutStatus;
import tn.manzel.commercee.DTO.PayoutSummaryDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface PayoutRepository extends JpaRepository<Payout,Long> {



    @Query("SELECT new tn.manzel.commercee.DTO.PayoutSummaryDTO(s.firstName, SUM(p.amount), s.rib) " +
            "FROM Payout p " +
            "JOIN p.seller s " +
            "WHERE p.status = :status " +
            "GROUP BY s.id, s.email, s.rib")
    List<PayoutSummaryDTO> getPayoutsByStatusGroupedBySellers(@Param("status") PayoutStatus status);

    List<Payout> findAllByStatus(PayoutStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Payout p SET p.status = 'COMPLETED', p.paidAt = :now " +
            "WHERE p.status = 'PENDING' " +
            "AND p.generatedAt >= :twoWeeksAgo")
    int updatePendingToCompleted(@Param("now") LocalDateTime now,
                                 @Param("twoWeeksAgo") LocalDateTime twoWeeksAgo);
}
}
