package tn.manzel.commercee.Service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.Product;
import tn.manzel.commercee.DAO.Entities.Mysql.User;
import tn.manzel.commercee.DAO.Repositories.Mysql.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository repository;



    public User findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }



    public void delete(String email) {

        User user = findByEmail(email);

        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        repository.save(user);
    }
}
