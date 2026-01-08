package tn.manzel.commercee.DAO.Entities.PostgresSql;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private AuditAction action;

    private String entity;

    private String entityId;

    private LocalDateTime timestamp;

    private boolean success;

    @Column(columnDefinition = "TEXT")
    private String message;
}
