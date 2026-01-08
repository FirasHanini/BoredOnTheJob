package tn.manzel.commercee.Aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tn.manzel.commercee.Service.AuditService.AuditService;
import tn.manzel.commercee.Service.AuditService.Auditable;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
    private final AuditService auditService;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        try {
            Object result = joinPoint.proceed();

            auditService.log(
                    username,
                    auditable.action(),
                    auditable.entity(),
                    extractEntityId(joinPoint.getArgs()),
                    true,
                    "SUCCESS"
            );

            return result;

        } catch (Exception ex) {

            auditService.log(
                    username,
                    auditable.action(),
                    auditable.entity(),
                    extractEntityId(joinPoint.getArgs()),
                    false,
                    ex.getMessage()
            );

            throw ex;
        }
    }

    private String extractEntityId(Object[] args) {
        if (args.length > 0) {
            return String.valueOf(args[0]);
        }
        return "N/A";
    }
}
