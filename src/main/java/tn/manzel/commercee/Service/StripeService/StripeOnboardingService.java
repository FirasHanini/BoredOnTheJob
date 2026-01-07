package tn.manzel.commercee.Service.StripeService;

import com.stripe.model.AccountLink;
import com.stripe.param.AccountLinkCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeOnboardingService {

    public String createOnboardingLink(String accountId) throws Exception {

        AccountLink link = AccountLink.create(
                AccountLinkCreateParams.builder()
                        .setAccount(accountId)
                        .setRefreshUrl("http://localhost:8080/onboarding/refresh")
                        .setReturnUrl("http://localhost:8080/onboarding/success")
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build()
        );

        return link.getUrl();
    }
}
