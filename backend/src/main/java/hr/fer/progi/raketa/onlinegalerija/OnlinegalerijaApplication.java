package hr.fer.progi.raketa.onlinegalerija;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableJpaRepositories
@SpringBootApplication
public class OnlinegalerijaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinegalerijaApplication.class, args);
	}
	
	
}
