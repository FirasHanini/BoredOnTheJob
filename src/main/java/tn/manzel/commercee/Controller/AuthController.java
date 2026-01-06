package tn.manzel.commercee.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import tn.manzel.commercee.DAO.Entities.Mysql.AuthResponse;
import tn.manzel.commercee.DAO.Entities.Mysql.LoginRequest;
import tn.manzel.commercee.DAO.Entities.Mysql.RegisterRequest;
import tn.manzel.commercee.Service.UserService.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request, authenticationManager);
        return ResponseEntity.ok(new AuthResponse(token));
    }


}
