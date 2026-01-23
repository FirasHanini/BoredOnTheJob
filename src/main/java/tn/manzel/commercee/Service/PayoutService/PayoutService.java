package tn.manzel.commercee.Service.PayoutService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;
import tn.manzel.commercee.DAO.Entities.Mysql.PayoutStatus;
import tn.manzel.commercee.DAO.Repositories.Mysql.PayoutRepository;
import tn.manzel.commercee.DTO.PayoutSummaryDTO;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PayoutService {

    private final PayoutRepository repository;

    public Payout savePayout(Payout payout) {
        return repository.save(payout);
    }


    public List<PayoutSummaryDTO> getPendingGroupedByseller() {
        return repository.getPayoutsByStatusGroupedBySellers(PayoutStatus.PENDING);
    }

    public List<Payout> getAllByStatus(PayoutStatus status) {
        return repository.findAllByStatus(status);
    }

}
