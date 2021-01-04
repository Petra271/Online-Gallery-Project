package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.dao.TransactionDTO;
import hr.fer.progi.raketa.onlinegalerija.model.Transaction;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import hr.fer.progi.raketa.onlinegalerija.repository.TransactionRepository;
import hr.fer.progi.raketa.onlinegalerija.repository.VisitorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionRepository transactionRepository;
    private VisitorRepository visitorRepository;
    private service service;

    public TransactionController(TransactionRepository transactionRepository, VisitorRepository visitorRepository, service service) {
        this.transactionRepository = transactionRepository;
        this.visitorRepository = visitorRepository;
        this.service = service;
    }

    @PostMapping("/addTransaction")
    public ResponseEntity<?> addNewTransaction (@RequestBody TransactionDTO transactionDTO) {
        transactionRepository.save(new Transaction(visitorRepository.findById(transactionDTO.getPayerId()).get(),
                                                   visitorRepository.findById(transactionDTO.getReceiverId()).get(),
                                                   transactionDTO.getTotalAmount(),
                                                   transactionDTO.getTotalAmount() * (1 - transactionDTO.getProvision()),
                                                transactionDTO.getProvision(),
                                                    transactionDTO.getTotalAmount() * transactionDTO.getProvision()));
        return new ResponseEntity<>("Transakcija uspjesno dodana!", HttpStatus.OK);
    }

    @GetMapping("/getAllTransactions")
    public ResponseEntity<?> getAllTransactions() {
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
