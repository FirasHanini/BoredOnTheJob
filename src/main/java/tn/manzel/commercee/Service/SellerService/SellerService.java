package tn.manzel.commercee.Service.SellerService;

import com.stripe.model.Account;
import com.stripe.param.AccountCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.RegisterRequest;
import tn.manzel.commercee.DAO.Entities.Mysql.Role;
import tn.manzel.commercee.DAO.Entities.Mysql.Seller;

import tn.manzel.commercee.DAO.Repositories.Mysql.SellerRepository;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Seller createSeller(RegisterRequest request) throws Exception {

        Account account = Account.create(
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .setEmail(request.email())
                        .build()
        );

        Seller seller = Seller.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.SELLER)
                .build();

        seller.setStripeAccountId(account.getId());

        return repository.save(seller);
    }

    public Seller getSellerById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
