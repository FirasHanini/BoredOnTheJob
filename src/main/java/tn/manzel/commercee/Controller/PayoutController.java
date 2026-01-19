package tn.manzel.commercee.Controller;


import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.Service.SMTPayementService.PayoutService;

@RestController
@RequestMapping(ApiEndpoints.PAYOUT_BASE)
@RequiredArgsConstructor
public class PayoutController {

    private final PayoutService payoutService;


    @GetMapping(ApiEndpoints.PayoutEndpoint.DOWNLOAD_PAYOUTS)
    public ResponseEntity<Resource> downloadPayoutFile() {

        Resource resource = payoutService.downloadPayoutFile();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=virements_vendeurs.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}
