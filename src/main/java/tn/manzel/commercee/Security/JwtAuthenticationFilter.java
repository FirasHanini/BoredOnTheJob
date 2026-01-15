package tn.manzel.commercee.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tn.manzel.commercee.DAO.Repositories.PostgresSql.RevokedTokensRepository;
import tn.manzel.commercee.Service.UserService.CustomUserDetailsService;
import tn.manzel.commercee.Service.UserService.JwtService;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@Component
public class    JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final RevokedTokensRepository revoked_tokens_repository;
    private final RequestAttributeSecurityContextRepository repo= new RequestAttributeSecurityContextRepository();
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            return;
        }

        String token = authHeader.substring(7);
        String jti = jwtService.extractJti(token);
        String username = jwtService.extractUsername(token);

        if (revoked_tokens_repository.existsByJti(jti)) {
            // Optionnel : log + audit
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );
            SecurityContext context= SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
            log.info("Authenticated user: " + username + " with token: " + token+" authToken: "+authToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            repo.saveContext(context, request, response);
        }

        filterChain.doFilter(request, response);
    }
}
