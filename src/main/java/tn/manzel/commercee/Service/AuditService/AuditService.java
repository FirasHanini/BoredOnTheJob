package tn.manzel.commercee.Service.AuditService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditLog;
import tn.manzel.commercee.DAO.Repositories.PostgresSql.AuditLogRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;

    public void log(String username,
                    AuditAction action,
                    String entity,
                    String entityId,
                    boolean success,
                    String message) {

        AuditLog log = AuditLog.builder()
                .username(username)
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .timestamp(LocalDateTime.now())
                .success(success)
                .message(message)
                .build();

        repository.save(log);
    }
}
