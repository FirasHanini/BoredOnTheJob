package tn.manzel.commercee.Service.SMTPayementService;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import tn.manzel.commercee.DAO.Entities.Mysql.Payout;
import tn.manzel.commercee.DAO.Entities.Mysql.Seller;
import tn.manzel.commercee.DAO.Repositories.Mysql.PayoutRepository;
import tn.manzel.commercee.DAO.Repositories.Mysql.SellerRepository;

import java.time.LocalDate;
import java.util.List;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class PayoutService {
    private final PayoutRepository payoutRepository;
    private final SellerRepository sellerRepository;


    public byte[] generateCsv(List<Payout> payouts) {
        StringBuilder csvContent = new StringBuilder();
        // En-tête du fichier  a revoir selon les demandes des banques
        csvContent.append("Nom Vendeur;RIB;Montant;Reference\n");

        for (Payout p : payouts) {
            csvContent.append(p.getSeller().getEmail()).append(";")
                    .append(p.getSeller().getRib()).append(";")
                    .append(p.getAmount()).append(";")
                    .append("VIR_S_").append(LocalDate.now()).append("\n");
        }

        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }


    public Resource downloadPayoutFile(){
        List<Payout> pendingPayouts = payoutRepository.findPendingWithSeller();
        byte[] data = generateCsv(pendingPayouts);

        return new ByteArrayResource(data);


    }


    public Payout getPayoutByEmail(String email){
        return payoutRepository.findById(1L).orElse(null);
    }

    public Payout savePayout(Payout payout){
        return payoutRepository.save(payout);
    }

    public Payout createPayout(Payout payout, String email){
        Seller seller = sellerRepository.findByEmail(email);
        payout.setSeller(seller);
        return savePayout(payout);
    }
}
