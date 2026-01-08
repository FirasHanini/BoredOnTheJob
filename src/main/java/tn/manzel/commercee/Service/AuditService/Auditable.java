package tn.manzel.commercee.Service.AuditService;

import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    AuditAction action();
    String entity();
}
