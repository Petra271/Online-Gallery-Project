package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.dao.LoginDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.VisitorRepository;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/visitor")
public class VisitorController {
    @Autowired
    private  VisitorRepository visitorRepository;



    @PostMapping("/registration")
    public ResponseEntity<?> registerNewVisitor(@RequestBody VisitorDTO visitorDTO){
        if(visitorRepository.existsByEmail(visitorDTO.getEmail())){
            return ResponseEntity.badRequest()
                    .body("User with this email already exists");
        }

        Visitor visitor = new Visitor(visitorDTO.getName(), visitorDTO.getSurname(), visitorDTO.getEmail(),
                                    visitorDTO.pswdEncoder().encode(visitorDTO.getPassword()), visitorDTO.getPaypalMail());

        visitorRepository.save(visitor);

        return ResponseEntity.ok()
                .body("Successful registration");
    }

    @PostMapping("/login")
    ResponseEntity<?> loginVisitor(@RequestBody LoginDTO loginDTO){
        if(loginDTO.pswdEncoder().matches(loginDTO.getPassword(), visitorRepository.findByEmail(loginDTO.getEmail()).getPassword())){
            return ResponseEntity.ok()
                    .body("Successful login");
        }
        return ResponseEntity.badRequest()
                .body("Invalid email/password");

    }
}
