package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.model.Artist;
import hr.fer.progi.raketa.onlinegalerija.repository.VisitorRepository;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    private  VisitorRepository visitorRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public VisitorController(VisitorRepository visitorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.visitorRepository = visitorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping(value="/registration", consumes={"multipart/form-data"})
    public ResponseEntity<?> registerNewVisitor(@RequestPart("json") VisitorDTO visitorDTO, @RequestPart("file") MultipartFile file) throws IOException {
        visitorDTO.setPassword(bCryptPasswordEncoder.encode(visitorDTO.getPassword()));
        if (!visitorRepository.existsByEmail(visitorDTO.getEmail())){
            if(visitorDTO.isFlag()){
                Artist artist = new Artist(visitorDTO.getName(), visitorDTO.getSurname(), visitorDTO.getEmail(),
                        visitorDTO.getPassword(), visitorDTO.getPaypalMail(), file.getBytes());
                artist.setRole("artist");
                visitorRepository.save(artist);
            } else {
                Visitor visitor = new Visitor(visitorDTO.getName(), visitorDTO.getSurname(), visitorDTO.getEmail(),
                        visitorDTO.getPassword(), visitorDTO.getPaypalMail());
                visitor.setRole("visitor");
                visitorRepository.save(visitor);
            }
        }

        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok()
                .body("Successful registration");
    }

}
