package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.ApiEndpoints.ApiEndpoints;
import tn.manzel.commercee.DAO.Entities.Mysql.AuthResponse;
import tn.manzel.commercee.DAO.Entities.Mysql.LoginRequest;
import tn.manzel.commercee.DAO.Entities.Mysql.RegisterRequest;
import tn.manzel.commercee.Service.SellerService.SellerService;
import tn.manzel.commercee.Service.UserService.AuthService;

@RestController
@RequestMapping(ApiEndpoints.AUTH_BASE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SellerService sellerService;

    @PostMapping(ApiEndpoints.AuuthEnpoints.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping(ApiEndpoints.AuuthEnpoints.LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request, authenticationManager);
        return ResponseEntity.ok(new AuthResponse(token));
    }



        @PostMapping(ApiEndpoints.AuuthEnpoints.REGISTER_SELLER)
    public ResponseEntity<?> createSeller(@RequestBody RegisterRequest request) throws Exception {
        sellerService.createSeller(request);
        return ResponseEntity.ok().body("seller Registered");
    }

}
