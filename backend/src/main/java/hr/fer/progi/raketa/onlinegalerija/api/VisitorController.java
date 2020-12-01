package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.repository.VisitorRepository;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    private  VisitorRepository visitorRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public VisitorController(VisitorRepository visitorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.visitorRepository = visitorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerNewVisitor(@RequestBody Visitor visitor)
    {
        visitor.setPassword(bCryptPasswordEncoder.encode(visitor.getPassword()));
        if (!visitorRepository.existsByEmail(visitor.getEmail()))
            visitorRepository.save(visitor);
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok()
                .body("Successful registration");
    }

}
