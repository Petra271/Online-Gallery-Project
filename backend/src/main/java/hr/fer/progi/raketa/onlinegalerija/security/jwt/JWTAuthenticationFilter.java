package hr.fer.progi.raketa.onlinegalerija.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import org.springframework.security.core.userdetails.User;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.EXPIRATION_TIME;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.HEADER_STRING;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.SECRET;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Visitor creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Visitor.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
       String token = JWT.create()
               .withSubject(((User) authResult.getPrincipal()).getUsername())
               .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .sign(Algorithm.HMAC512(SECRET.getBytes()));
       response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
