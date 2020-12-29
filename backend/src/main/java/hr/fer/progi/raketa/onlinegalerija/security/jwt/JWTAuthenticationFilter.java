package hr.fer.progi.raketa.onlinegalerija.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.raketa.onlinegalerija.model.Admin;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import hr.fer.progi.raketa.onlinegalerija.repository.Roles;
import hr.fer.progi.raketa.onlinegalerija.repository.VisitorRepository;
import hr.fer.progi.raketa.onlinegalerija.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import org.springframework.security.core.userdetails.User;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.EXPIRATION_TIME;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.HEADER_STRING;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.SECRET;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Visitor creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Visitor.class);

            UserDetails userDetails = userDetailsService.loadUserByUsername(creds.getEmail());


            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            userDetails.getAuthorities()));

            if(!userDetails.getAuthorities().iterator().next().getAuthority().equals(Roles.ADMIN.toString().toLowerCase()))
                auth.setAuthenticated(false);

            return auth;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

       String token = JWT.create()
               .withSubject(((User) authResult.getPrincipal()).getUsername())
               .withClaim("role", ((List<SimpleGrantedAuthority>) authResult.getAuthorities()).get(0).getAuthority())
               .withClaim("admin", authResult.isAuthenticated())
               .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .sign(Algorithm.HMAC512(SECRET.getBytes()));

       response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

    }
}
