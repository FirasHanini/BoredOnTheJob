package tn.manzel.commercee.DAO.Entities.Mysql;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;


@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@SQLRestriction("deleted = false")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;


    @Column(unique = true)
    private String email;

    @NotNull
    @Length(min = 3)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
