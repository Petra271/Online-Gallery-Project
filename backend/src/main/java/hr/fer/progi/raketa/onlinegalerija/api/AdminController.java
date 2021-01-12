package hr.fer.progi.raketa.onlinegalerija.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.raketa.onlinegalerija.dao.ContestDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.ExhibitionDTO;
import hr.fer.progi.raketa.onlinegalerija.model.*;
import hr.fer.progi.raketa.onlinegalerija.model.Collection;
import hr.fer.progi.raketa.onlinegalerija.repository.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@Controller
@RequestMapping("/admin")
public class AdminController {
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
    private ExhibitionRepository exhibitionRepository;
    @Autowired
    private service service;

    @PostMapping("/createContest")
    public ResponseEntity<?> createContest(@RequestBody ContestDTO contestDTO){
        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        if(contestRepository.existsByWorkingName(contestDTO.getName()))
            return new ResponseEntity<String>("A contest of the same name already exists", HttpStatus.FORBIDDEN);

        if(!adminRepository.existsByEmail(currentUsername))
            return new ResponseEntity<String>("No admin with this username exists", HttpStatus.NOT_FOUND);

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Contest contest = new Contest(
                LocalDateTime.parse(contestDTO.getBeginDateTime(), f),
                Duration.parse(contestDTO.getDuration()),
                contestDTO.getName(),
                Style.valueOf(contestDTO.getStyle()),
                contestDTO.getDescription(),
                contestDTO.getProvision()
                );
        System.out.println(contestDTO.getProvision());

        contestRepository.save(contest);
        return ResponseEntity.ok().body("Successful contest creation");
    }


    @PostMapping("/createExhibition")
    public ResponseEntity<?> createExhibition(@RequestPart("contestName") String contestName,
                                  @RequestPart("exName") String exName,
                                  @RequestPart("exDesc") String exDesc,
                                  @RequestPart("desc") String accepted) throws JsonProcessingException {

        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        if(exhibitionRepository.existsByName(exName))
            return new ResponseEntity<>("An exhibition of the same name already exists", HttpStatus.NOT_ACCEPTABLE);

        if(!adminRepository.existsByEmail(currentUsername))
            return new ResponseEntity<String>("No admin with this username exists", HttpStatus.NOT_FOUND);


        Map<String, String[]> acceptedCollections = new ObjectMapper().readValue(accepted, new  TypeReference<Map<String, String[]>>(){});

        Set<Artist> artists = new HashSet<>();
        Set<Collection> collections = new HashSet<>();

        Contest contest = contestRepository.findByWorkingName(contestName);
        if(contest == null)
            return new ResponseEntity<String>("No contest with this name exists", HttpStatus.NOT_FOUND);

        Exhibition ex = new Exhibition(
                contest.getBeginDateTime(),
                contest.getDuration(),
                exName,
                contest.getStyle(),
                exDesc,
                contest.getProvision()
        );
        System.out.println(contest.getProvision());

        for(Map.Entry<String, String[]> e : acceptedCollections.entrySet()){
            Artist artist = artistRepository.findByEmail(e.getKey());
            artist.addExhibition(ex);
            artists.add(artist);
            for(String s : e.getValue()) {
                Collection collection = artist.getCollectionByName(s);
                collection.setExhibition(ex);
                collections.add(collection);
                System.out.println(collection.getName());
            }
        }
        System.out.println(collections.size());
        ex.setArtists(artists);
        ex.setCollections(collections);
        exhibitionRepository.save(ex);
        return ResponseEntity.ok().body("Successfully added exhibition");

    }

    @GetMapping(value= "/getApplications", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getApplications(@RequestParam("contestName") String contestName){
        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        if(!adminRepository.existsByEmail(currentUsername))
            return new ResponseEntity<String>("No admin with this username exists", HttpStatus.NOT_FOUND);

        if(!contestRepository.existsByWorkingName(contestName))
            return new ResponseEntity<String>("No contest with this working name exists", HttpStatus.NOT_FOUND);

        return service.produceApplications(contestRepository.findByWorkingName(contestName));
    }

    @PostMapping("/closeExhibition")
    public ResponseEntity<?> closeExhibition(@RequestParam("exhName") String exhName){
        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        if(!adminRepository.existsByEmail(currentUsername))
            return new ResponseEntity<String>("No admin with this username exists", HttpStatus.NOT_FOUND);

        if(!exhibitionRepository.existsByName(exhName))
            return new ResponseEntity<String>("No exhibition with this name exists", HttpStatus.NOT_FOUND);

        exhibitionRepository.delete(exhibitionRepository.findByName(exhName));

        return ResponseEntity.ok().body("Successfully closed exhibition");
    }
}
