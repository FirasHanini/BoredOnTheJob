package tn.manzel.commercee.DAO.Repositories.PostgresSql;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tn.manzel.commercee.DAO.Entities.PostgresSql.RevokedTokens;

public interface RevokedTokensRepository extends JpaRepository<RevokedTokens, Long> {
    boolean existsByJti(String jti);

    @Modifying
    @Transactional
    @Query("DELETE FROM RevokedTokens r WHERE r.ExpiredAt < CURRENT_TIMESTAMP")
    int deleteExpiredTokens();
}
