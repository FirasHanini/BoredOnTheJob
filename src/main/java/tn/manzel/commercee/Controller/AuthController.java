package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.Mysql.AuthResponse;
import tn.manzel.commercee.DAO.Entities.Mysql.LoginRequest;
import tn.manzel.commercee.DAO.Entities.Mysql.RegisterRequest;
import tn.manzel.commercee.DAO.Entities.PostgresSql.AuditAction;
import tn.manzel.commercee.Service.AuditService.Auditable;
import tn.manzel.commercee.Service.SellerService.SellerService;
import tn.manzel.commercee.Service.UserService.AuthService;

@RestController
@RequestMapping(ApiEndpoints.AUTH_BASE)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SellerService sellerService;

    @Auditable(action = AuditAction.CREATE, entity = "USER")
    @PostMapping(ApiEndpoints.AuthEnpoints.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered");
    }

    @Auditable(action = AuditAction.LOGIN, entity = "USER")
    @PostMapping(ApiEndpoints.AuthEnpoints.LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request, authenticationManager);
        return ResponseEntity.ok(
                AuthResponse.builder()
                .token(token)
                .build());
    }



    @Auditable(action = AuditAction.CREATE, entity = "USER")
    @PostMapping(ApiEndpoints.AuthEnpoints.REGISTER_SELLER)
    public ResponseEntity<?> createSeller(@RequestBody RegisterRequest request) throws Exception {
        sellerService.createSeller(request);
        return ResponseEntity.ok().body("seller Registered");
    }

    @Auditable(action = AuditAction.LOGOUT, entity = "USER")
    @PostMapping(ApiEndpoints.AuthEnpoints.LOGOUT)
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String auth){
        if (!authService.logout(auth))
            return ResponseEntity.status(400).build();
        return ResponseEntity.ok().build();
        }

    }


