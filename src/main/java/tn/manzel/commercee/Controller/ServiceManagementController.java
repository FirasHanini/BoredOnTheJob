package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;
import tn.manzel.commercee.Service.AuditService.Auditable;
import tn.manzel.commercee.Service.UserService.UserManagementService;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiEndpoints.USR_MANAGEMENT_BASE)
public class ServiceManagementController {

    private final UserManagementService service;

    @Auditable(action = AuditAction.DELETE, entity = "USER")
    @DeleteMapping("/{email}")
    public ResponseEntity<String> delete(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok().body("User deleted");
    }
}
