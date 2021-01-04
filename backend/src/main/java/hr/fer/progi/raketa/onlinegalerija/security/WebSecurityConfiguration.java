package hr.fer.progi.raketa.onlinegalerija.security;

import hr.fer.progi.raketa.onlinegalerija.security.jwt.JWTAuthenticationFilter;
import hr.fer.progi.raketa.onlinegalerija.security.jwt.JWTAuthorizationFilter;
import hr.fer.progi.raketa.onlinegalerija.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

import static hr.fer.progi.raketa.onlinegalerija.security.SecurityConstants.*;


@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;
    public static Map<String, String> loggedInUsers;

    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService){
        this.userDetailsService = userDetailsService;
        loggedInUsers = new HashMap<>();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, LOGOUT).permitAll()
                .antMatchers(HttpMethod.POST, COLLECTION_CREATION).permitAll()
                .antMatchers(HttpMethod.POST, ADD_ARTWORK).permitAll()
                .antMatchers(HttpMethod.POST, ADD_COMMENT).permitAll()
                .antMatchers(HttpMethod.POST, ADD_CONTEST).permitAll()
                .antMatchers(HttpMethod.POST, ADD_EXHIBITION).permitAll()
                .antMatchers(HttpMethod.POST, APPLY_TO_CONTEST).permitAll()
                .antMatchers(HttpMethod.GET, GET_COLLECTIONS).permitAll()
                .antMatchers(HttpMethod.GET, GET_EXHIBITION).permitAll()
                .antMatchers(HttpMethod.POST, TEST).permitAll()
                .antMatchers(HttpMethod.GET, GET_COMMENTS).permitAll()
                .antMatchers(HttpMethod.DELETE, DELETE_COMMENT).permitAll()
                .antMatchers(HttpMethod.POST, ADD_TRANSACTION).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_TRANSACTIONS).permitAll()
                .antMatchers(HttpMethod.GET, GET_ALL_TRANSACTIONS_BY_USER).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), this.userDetailsService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    @Primary
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static String hash(String password,int row) {
        return BCrypt.hashpw(password, BCrypt.gensalt(row));
    }

}
