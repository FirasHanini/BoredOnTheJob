package tn.manzel.commercee.Schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.manzel.commercee.DAO.Repositories.PostgresSql.RevokedTokensRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RevokedTokensCleanUp {

    private final RevokedTokensRepository repository;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredTokens() {

        int deleted = repository.deleteExpiredTokens();

        if (deleted > 0) {
            log.info(" Revoked tokens cleaned: {} entries removed", deleted);
        }
    }




}
