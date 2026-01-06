    package tn.manzel.commercee.Service.UserService;

    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import tn.manzel.commercee.DAO.Entities.Mysql.LoginRequest;
    import tn.manzel.commercee.DAO.Entities.Mysql.RegisterRequest;
    import tn.manzel.commercee.DAO.Entities.Mysql.Role;
    import tn.manzel.commercee.DAO.Entities.Mysql.User;
    import tn.manzel.commercee.DAO.Repositories.Mysql.UserRepository;

    @Service
    @RequiredArgsConstructor
    public class AuthService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;



        public void register(RegisterRequest request) {
            User user = User.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        public String login(LoginRequest request, AuthenticationManager authManager) {

            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(), request.password())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            return jwtService.generateToken(userDetails);
        }
    }
