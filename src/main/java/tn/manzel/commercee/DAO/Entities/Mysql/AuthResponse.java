package tn.manzel.commercee.DAO.Entities.Mysql;

import lombok.Builder;

@Builder
public record AuthResponse(String token) {
}
