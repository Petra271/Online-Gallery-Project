package hr.fer.progi.raketa.onlinegalerija.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import hr.fer.progi.raketa.onlinegalerija.repository.Roles;
import hr.fer.progi.raketa.onlinegalerija.service.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.List;

import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.EXPIRATION_TIME;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.HEADER_STRING;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.SECRET;
import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.TOKEN_PREFIX;
import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private boolean loggedIn;
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
            if(loggedInUsers.containsValue(creds.getEmail())){
                loggedIn = true;
                return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                "failed",
                                "failed",
                                new ArrayList<>()));
            }
            loggedIn = false;

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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
       String token = JWT.create()
               .withSubject(((User) authResult.getPrincipal()).getUsername())
               .withClaim("role", ((List<SimpleGrantedAuthority>) authResult.getAuthorities()).get(0).getAuthority())
               .withClaim("admin", authResult.isAuthenticated())
               .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .sign(Algorithm.HMAC512(SECRET.getBytes()));
//       System.out.println(((User) authResult.getPrincipal()).getUsername());
//       if(authResult instanceof AnonymousAuthenticationToken)
//           System.out.println("Is anon");
       loggedInUsers.put(token, ((User) authResult.getPrincipal()).getUsername());
       response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        try {
            response.getWriter().write(token);
            String role = ((List<SimpleGrantedAuthority>) authResult.getAuthorities()).get(0).getAuthority();
            System.out.println(role);
            int res = 0;
            switch (role){
                case "artist":
                    res = 2; break;
                case "admin":
                    res = 1; break;
            }
            response.getWriter().write(" " + res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        if(loggedIn) {
            response.getWriter().write("This user is already logged in");
            System.out.println("A user that is already logged in has tried to log in");
        }
    }
//    @Override
//    protected void onAuthenticationFailure(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            AuthenticationException exception)
//            throws IOException, ServletException {
//
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        if(loggedIn)
//            response.getWriter().write("This user is already logged in");
//
//    }
}
