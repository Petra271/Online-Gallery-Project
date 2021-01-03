package hr.fer.progi.raketa.onlinegalerija.api;

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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@Controller
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    private VisitorRepository visitorRepository;
    private ArtistRepository artistRepository;
    private CollectionRepository collectionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ArtworkRepository artworkRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private service service;

    public VisitorController(VisitorRepository visitorRepository, ArtistRepository artistRepository,
                             CollectionRepository collectionRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                             ArtworkRepository artworkRepository) {
        this.visitorRepository = visitorRepository;
        this.artistRepository = artistRepository;
        this.collectionRepository = collectionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.artworkRepository = artworkRepository;
        this.service = new service();
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
            return new ResponseEntity<String>("This E-mail is taken", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok()
                .body("Successful registration");
    }

    @PostMapping("/createCollection")
    public ResponseEntity<?> createCollection(@RequestBody CollectionDTO collectionDTO){

        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        Artist artist = artistRepository.findByEmail(currentUsername);
        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        List<Collection> collectionList = collectionRepository
                .findByArtist(artist);
        for(Collection c : collectionList)
            if(c.getName().equals(collectionDTO.getName()))
                return new ResponseEntity<String>("Artist already has a collection of the same name", HttpStatus.NOT_ACCEPTABLE);
        //System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        Style style;
        try{
            style = Style.valueOf(collectionDTO.getStyle());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>("Style does not exist", HttpStatus.NOT_ACCEPTABLE);
        }
        Collection collection = new Collection(collectionDTO.getName(), collectionDTO.getDescription(), style, artist);

        collectionRepository.save(collection);
        return ResponseEntity.ok().body("Successful collection creation");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){

        loggedInUsers.remove(BearerTokenUtil.getBearerTokenHeader());
        return ResponseEntity.ok().body("Logout successful");
    }


    @PostMapping(value="/addArtwork", consumes={"multipart/form-data"})
    public ResponseEntity<?> addArtwork(@RequestPart("json") ArtworkDTO artworkDTO, @RequestPart("file") MultipartFile file) throws IOException{

        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        List<Collection> collectionList = collectionRepository
                .findByArtist(artist);

        Artwork artwork = null;
        for(Collection c : collectionList)
            if(c.getName().equals(artworkDTO.getCollectionName())) {
                artwork = new Artwork(
                        artworkDTO.getName(),
                        artworkDTO.getDescription(),
                        Style.valueOf(artworkDTO.getStyle()),
                        Double.parseDouble(artworkDTO.getPrice()),
                        file.getBytes(),
                        c
                );
                c.addArtwork(artwork);
            }

        if(artwork == null)
            return new ResponseEntity<String>("This artist does not contain the collection this artwork is supposed be inserted in.", HttpStatus.NOT_ACCEPTABLE);

        artworkRepository.save(artwork);

        return ResponseEntity.ok().body("successfully added artwork");
    }

    @GetMapping(value="/getCollections")
    @ResponseBody
    public ResponseEntity<?> getCollections() throws JSONException {
        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        Set<Collection> collectionSet = artist.getCollections();
                //collectionRepository.findByArtist(artist);

        return service.produceCollections(collectionSet);
    }

    @PostMapping(value="/test")
    @ResponseBody
    public ResponseEntity<?> test(){
        //adminRepository.deleteById(UUID.fromString("8bf731e0-4249-4e50-83a7-0671294d814f"));
        adminRepository.save(new Admin("admin", "admin", "admin1@gmail.com", bCryptPasswordEncoder.encode("password"), "nekimail"));

        return ResponseEntity.ok().body("test results");

    }
}
