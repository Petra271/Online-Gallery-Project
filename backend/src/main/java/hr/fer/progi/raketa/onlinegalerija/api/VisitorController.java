package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.dao.CollectionDTO;
import hr.fer.progi.raketa.onlinegalerija.model.Artist;
import hr.fer.progi.raketa.onlinegalerija.model.Collection;
import hr.fer.progi.raketa.onlinegalerija.model.Style;
import hr.fer.progi.raketa.onlinegalerija.repository.ArtistRepository;
import hr.fer.progi.raketa.onlinegalerija.repository.CollectionRepository;
import hr.fer.progi.raketa.onlinegalerija.repository.VisitorRepository;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    private VisitorRepository visitorRepository;
    private ArtistRepository artistRepository;
    private CollectionRepository collectionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public VisitorController(VisitorRepository visitorRepository, ArtistRepository artistRepository,
                             CollectionRepository collectionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.visitorRepository = visitorRepository;
        this.artistRepository = artistRepository;
        this.collectionRepository = collectionRepository;
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
                artistRepository.save(artist);
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

    @PostMapping("/createCollection")
    public ResponseEntity<?> createCollection(@RequestBody CollectionDTO collectionDTO){
        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        Artist artist = artistRepository.findByEmail(currentUsername);
        List<Collection> collectionList = collectionRepository
                .findByArtist(artist);
        for(Collection c : collectionList)
            if(c.getName().equals(collectionDTO.getName()))
                return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        //System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        Style style;
        try{
            style = Style.valueOf(collectionDTO.getStyle());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Collection collection = new Collection(collectionDTO.getName(), collectionDTO.getDescription(), style, artist);
        collectionRepository.save(collection);
        return ResponseEntity.ok().body("Successful collection creation");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        loggedInUsers.remove(BearerTokenUtil.getBearerTokenHeader());
        return ResponseEntity.ok().body("test successful");
    }

    @PostMapping("/test") public ResponseEntity<?> test(){
        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));
        if(artist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Collection> collectionList = collectionRepository
                .findByArtist(artist);
        for(Collection c : collectionList)
            System.out.println(c.getName());
        return ResponseEntity.ok().body("test results");
    }
}
