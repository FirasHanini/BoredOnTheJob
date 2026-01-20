package tn.manzel.commercee.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.Service.KonnektPaymentService.KonnektPaymentService;

import java.util.Map;

@RestController
@RequestMapping(ApiEndpoints.KONNEKT_BASE)
@RequiredArgsConstructor
public class KonnektPaymentController {

    private final KonnektPaymentService konnektPaymentService;


    @PostMapping(ApiEndpoints.konnektEndpoints.INIT_PAYMENT)
    public Map<String, Object> initierPaiement(@RequestParam Double montant, Authentication auth) {
        String description="Payment From"+auth.getName();
        return konnektPaymentService.initierPaiement(montant, description);
    }
}
