package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.dao.TransactionDTO;
import hr.fer.progi.raketa.onlinegalerija.model.Artist;
import hr.fer.progi.raketa.onlinegalerija.model.Artwork;
import hr.fer.progi.raketa.onlinegalerija.model.Transaction;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import hr.fer.progi.raketa.onlinegalerija.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
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
    private ApplicationRepository applicationRepository;
    private service service;

    public TransactionController(TransactionRepository transactionRepository, VisitorRepository visitorRepository, service service) {
        this.transactionRepository = transactionRepository;
        this.visitorRepository = visitorRepository;
        this.service = service;
    }

    @PostMapping("/addTransaction")
    public ResponseEntity<?> addNewTransaction (@RequestParam("artworkId") UUID artworkId, @RequestParam("provision") double provision) {
        String currentEmail = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());
        
        if(!visitorRepository.existsByEmail(currentEmail))
            return new ResponseEntity<>("Korisnik s emailom" + currentEmail + "ne postoji", HttpStatus.NOT_FOUND);

        Visitor payer = visitorRepository.findByEmail(currentEmail);
        Artwork artwork = artworkRepository.findById(artworkId).get();

        if(artwork.getTransaction() == null)
            return new ResponseEntity<>("Ovo djelo je već kupljeno", HttpStatus.NOT_ACCEPTABLE);

        Artist artist = artwork.getCollection().getArtist();

        double amountToArtist = artwork.getPrice() - (provision/100) * artwork.getPrice();

        Transaction transaction = new Transaction(
                payer,
                artist,
                artwork.getPrice(),
                amountToArtist,
                provision,
                (provision/100) * artwork.getPrice(),
                artwork
        );
        payer.addTransactionAsPayer(transaction);
        visitorRepository.save(payer);
        artist.addTransactionAsReceiver(transaction);
        visitorRepository.save(artist);
        artwork.setTransaction(transaction);
        artworkRepository.save(artwork);

        transactionRepository.save(transaction);
        return new ResponseEntity<>("Transakcija uspjesno dodana!", HttpStatus.OK);
    }

    @GetMapping("/getAllTransactions")
    public ResponseEntity<?> getAllTransactions() {
        String currentUsername = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());

        if(!adminRepository.existsByEmail(currentUsername))
            return new ResponseEntity<String>("No admin with this username exists", HttpStatus.NOT_FOUND);

        return service.produceTransactions(transactionRepository.findAll());
    }

    @GetMapping("/getAllTransactionsByUser")
    public ResponseEntity<?> getAllUserTransactions() {
        String currentEmail = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());
        Visitor visitor = null;
        if (visitorRepository.existsByEmail(currentEmail)) {
            visitor = visitorRepository.findByEmail(currentEmail);
        } else {
            return new ResponseEntity<>("Korisnik s emailom" + currentEmail + "ne postoji", HttpStatus.BAD_REQUEST);
        }

        return service.produceTransactions(transactionRepository.findTransactionsByUser(visitor.getId()));
    }
}
