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
import tn.manzel.commercee.Service.StripeService.StripeOnboardingService;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final StripeOnboardingService stripeOnBoardingService;

    public String createSeller(RegisterRequest request) throws Exception {

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

        String acountId= account.getId();
        seller.setStripeAccountId(acountId);

        return stripeOnBoardingService.createOnboardingLink(acountId);
    }

    public Seller getSellerById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
