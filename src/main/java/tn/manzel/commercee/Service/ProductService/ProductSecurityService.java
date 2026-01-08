package tn.manzel.commercee.Service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.User;
import tn.manzel.commercee.DAO.Repositories.Mysql.ProductRepository;
import tn.manzel.commercee.Service.UserService.UserManagementService;

@Service
@RequiredArgsConstructor
public class ProductSecurityService {

    private final UserManagementService userService;
    private final ProductRepository productRepository;

    public boolean isOwner (Long productId, Authentication authentication){
//        User user = (User) authentication.getPrincipal();

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        if(user == null){
            return false;
        }
        return productRepository.existsByIdAndSellerId(
                productId,
                user.getId());
    }
}
