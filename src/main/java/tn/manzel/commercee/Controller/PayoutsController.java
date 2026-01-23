package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;
import tn.manzel.commercee.DAO.Entities.Mysql.PayoutStatus;
import tn.manzel.commercee.Service.PayoutService.PayoutService;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.PAYOUT_BASE)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class PayoutsController {
    private final PayoutService payoutService;

    @GetMapping
    public ResponseEntity<List<Payout>> getPending() {
        return ResponseEntity.ok().body( payoutService.getAllByStatus(PayoutStatus.PENDING));
    }
}
