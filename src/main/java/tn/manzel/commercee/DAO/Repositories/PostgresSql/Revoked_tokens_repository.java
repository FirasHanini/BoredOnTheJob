package tn.manzel.commercee.DAO.Repositories.PostgresSql;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.manzel.commercee.DAO.Entities.PostgresSql.Revoked_tokens;

public interface Revoked_tokens_repository extends JpaRepository<Revoked_tokens, Long> {
}
