package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.Service.SellerService.SellerService;
import tn.manzel.commercee.Service.StripeService.StripeOnboardingService;

@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final StripeOnboardingService onboardingService;
    private final SellerService sellerService;

    @GetMapping("/{sellerId}")
    public ResponseEntity<String> onboard(@PathVariable Long sellerId) throws Exception {

        var seller = sellerService.getSellerById(sellerId);
        if (seller == null) {
                return ResponseEntity.badRequest().body("No such seller");
        }


        return ResponseEntity.ok().body(onboardingService.createOnboardingLink(
                seller.getStripeAccountId())
        );
    }

    @GetMapping("/success")
    public String success() {
        return "Onboarding completed";
    }

    @GetMapping("/refresh")
    public String refresh() {
        return "Retry onboarding";
    }

}
