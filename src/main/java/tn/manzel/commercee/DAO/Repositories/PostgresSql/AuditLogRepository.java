package tn.manzel.commercee.DAO.Repositories.PostgresSql;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
