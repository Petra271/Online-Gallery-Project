package hr.fer.progi.raketa.onlinegalerija.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import hr.fer.progi.raketa.onlinegalerija.dao.*;
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

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@Controller
@RequestMapping("/artist")


public class ArtistController {
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ContestApplicationRepository contestApplicationRepository;
    @Autowired
    private service service;

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
                for(Artwork a : c.getArtworks())
                    if (a.getName().equals(artworkDTO.getName()))
                        return new ResponseEntity<>("An artwork of the same name already exists", HttpStatus.NOT_ACCEPTABLE);
                artwork = new Artwork(
                        artworkDTO.getName(),
                        artworkDTO.getDescription(),
                        Style.valueOf(artworkDTO.getStyle()),
                        Double.parseDouble(artworkDTO.getPrice()),
                        file.getBytes(),
                        file.getContentType(),
                        c
                );
                c.addArtwork(artwork);

            }

        if(artwork == null)
            return new ResponseEntity<String>("This artist does not contain the collection this artwork is supposed be inserted in.", HttpStatus.NOT_ACCEPTABLE);

        artworkRepository.save(artwork);

        return ResponseEntity.ok().body("Artwork successfully added");
    }

    @PostMapping("/removeCollection")
    public ResponseEntity<?> removeArtwork(@RequestBody String collName){
        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        for(Collection c : artist.getCollections())
            if (c.getName().equals(collName)) {
                for (Artwork a : c.getArtworks())
                    artworkRepository.delete(a);
                collectionRepository.deleteById(c.getId());
                return new ResponseEntity<String>("Collection successfully removed", HttpStatus.OK);
            }

        return new ResponseEntity<String>("Collection not found", HttpStatus.NOT_FOUND);
    }


    @PostMapping(value="/removeArtwork")
    public ResponseEntity<?> removeArtwork(@RequestBody RemArtDTO remArtDTO){
        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        for(Collection c : artist.getCollections())
            if (c.getName().equals(remArtDTO.getCollName()))
                for (Artwork a : c.getArtworks())
                    if (a.getName().equals(remArtDTO.getArtworkName())) {
                        artworkRepository.delete(a);
                        return new ResponseEntity<String>("Artwork successfully removed", HttpStatus.OK);
                    }

        return new ResponseEntity<String>("Artwork not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/getCollections", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getCollections(@RequestParam("type") String collNum) throws JSONException, JsonProcessingException {
        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        Set<Collection> collections = artist.getCollections();
        //collectionRepository.findByArtist(artist);
        System.out.println(collNum);
        return collNum.equals("all") ? service.produceCollections(collections) : service.produceCollectionsSingles(collections);
    }
    @GetMapping(value="/getCollection", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getCollection(@RequestParam("name") String collName) throws JSONException, JsonProcessingException {
        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        for(Collection c : artist.getCollections())
            if(c.getName().equals(collName))
                return service.produceCollection(c);

        System.out.println(collName);
        return new ResponseEntity<String>("This artist does not have a collection by the name of " + collName, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/getCollectionsList", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getCollectionList() throws JSONException, JsonProcessingException {

        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        Set<Collection> collections = artist.getCollections();

        return service.produceCollectionsList(collections);
    }

    @PostMapping("/applyToContest")
    public ResponseEntity<?> applyToContest(@RequestBody ApplicationDTO applicationDTO){
        Artist artist = artistRepository.findByEmail(loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader()));

        if(artist == null)
            return new ResponseEntity<String>("Artist was not found in the repository", HttpStatus.NOT_FOUND);

        Contest contest = contestRepository.findByWorkingName(applicationDTO.getContestName());
        if(contest == null)
            return new ResponseEntity<String>("No contest of name " + applicationDTO.getContestName() + " exists", HttpStatus.NOT_FOUND);

        ContestApplication ca = new ContestApplication(artist, contest);
        Set<Collection> collectionSetApplied = new HashSet<>();
        ca.setCollections(collectionSetApplied);
        String[] collectionNames = applicationDTO.getCollections();

        for(int i = 0; i < collectionNames.length; i++)
            for(Collection c : artist.getCollections())
                if(c.getName().equals(collectionNames[i])) {
                    System.out.println("Applying " + c.getName() +" to contest");
                    collectionSetApplied.add(c);
                    c.setContestApplication(ca);
                }

        contest.addApplication(ca);
        contestApplicationRepository.save(ca);

        return ResponseEntity.ok().body("Successfully applied to contest");
    }
}
