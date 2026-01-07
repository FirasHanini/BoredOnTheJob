package tn.manzel.commercee.DAO.Entities.Mysql;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String email;

    @NotNull
    @Length(min = 3)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
