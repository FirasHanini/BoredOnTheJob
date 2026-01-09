package tn.manzel.commercee.DAO.Entities.PostgresSql;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RevokedTokens {

    @Id
    String jti;

    @NotNull
    Date ExpiredAt;

}
