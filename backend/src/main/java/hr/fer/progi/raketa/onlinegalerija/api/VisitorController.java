package hr.fer.progi.raketa.onlinegalerija.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import hr.fer.progi.raketa.onlinegalerija.dao.CollectionDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.ArtworkDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.VisitorDTO;
import hr.fer.progi.raketa.onlinegalerija.model.*;
import hr.fer.progi.raketa.onlinegalerija.repository.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@Controller
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private ArtworkRepository artworkRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ExhibitionRepository exhibitionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private service service;

    public VisitorController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @PostMapping(value="/registration", consumes={"multipart/form-data"})
    public ResponseEntity<?> registerNewVisitor(@RequestPart("json") VisitorDTO visitorDTO, @RequestPart(name="file", required = false) MultipartFile file) throws IOException {

        visitorDTO.setPassword(bCryptPasswordEncoder.encode(visitorDTO.getPassword()));

        if (!visitorRepository.existsByEmail(visitorDTO.getEmail())){
            if(visitorDTO.isFlag()){
                Artist artist = new Artist(visitorDTO.getName(), visitorDTO.getSurname(), visitorDTO.getEmail(),
                        visitorDTO.getPassword(), visitorDTO.getPaypalMail(), file.getBytes());
                artist.setRole(Roles.ARTIST.toString().toLowerCase());
                artistRepository.save(artist);
            } else {
                Visitor visitor = new Visitor(visitorDTO.getName(), visitorDTO.getSurname(), visitorDTO.getEmail(),
                        visitorDTO.getPassword(), visitorDTO.getPaypalMail());
                visitor.setRole(Roles.VISITOR.toString().toLowerCase());
                visitorRepository.save(visitor);
            }
        }

        else{
            return new ResponseEntity<String>("This E-mail is taken", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok()
                .body("Successful registration");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){

        loggedInUsers.remove(BearerTokenUtil.getBearerTokenHeader());
        return ResponseEntity.ok().body("Logout successful");
    }

    @GetMapping(value="/getExhibition", produces = "application/json")
    public ResponseEntity<?> getExhibition(@RequestParam("exName") String exName) throws JsonProcessingException {
        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        if(!visitorRepository.existsByEmail(currentUsername))
            return new ResponseEntity<String>("No visitor with this username exists", HttpStatus.NOT_FOUND);

        if(!exhibitionRepository.existsByName(exName))
            return new ResponseEntity<String>("No exhibition with this name exists", HttpStatus.NOT_FOUND);

        Exhibition ex = exhibitionRepository.findByName(exName);

        return service.produceExhibition(ex);
    }

    @GetMapping(value="/getExhibitionSingles", produces = "application/json")
    public ResponseEntity<?> getExhibitionSingles() throws JsonProcessingException {
        if(exhibitionRepository.findAll().size() == 0)
            return ResponseEntity.ok().body("ok");
        return service.produceExhibitionSingles(new HashSet<>(exhibitionRepository.findAll()));
    }

    @GetMapping(value="/getContests", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getContests() throws JsonProcessingException {
        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        if(!artistRepository.existsByEmail(currentUsername) && !adminRepository.existsByEmail(currentUsername))
            return new ResponseEntity<String>("No admin or artist with this username exists", HttpStatus.NOT_FOUND);

        return service.produceContests(new HashSet<Contest>(contestRepository.findAll()));
    }

    @PostMapping(value="/test")
    @ResponseBody
    public ResponseEntity<?> test(){
        //adminRepository.deleteById(UUID.fromString("8bf731e0-4249-4e50-83a7-0671294d814f"));
        if(!adminRepository.existsByEmail("admin1@gmail.com"))
            adminRepository.save(new Admin("admin", "admin", "admin1@gmail.com", bCryptPasswordEncoder.encode("password"), "nekimail"));

        return ResponseEntity.ok().body("test results");
    }
}
