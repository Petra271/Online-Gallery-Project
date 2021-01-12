package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.dao.ArtworkIdDTO;
import hr.fer.progi.raketa.onlinegalerija.dao.CommentDTO;
import hr.fer.progi.raketa.onlinegalerija.model.Artwork;
import hr.fer.progi.raketa.onlinegalerija.model.Comment;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import hr.fer.progi.raketa.onlinegalerija.repository.ArtistRepository;
import hr.fer.progi.raketa.onlinegalerija.repository.ArtworkRepository;
import hr.fer.progi.raketa.onlinegalerija.repository.CommentRepository;
import hr.fer.progi.raketa.onlinegalerija.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static hr.fer.progi.raketa.onlinegalerija.security.WebSecurityConfiguration.loggedInUsers;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private CommentRepository commentRepository;
    private VisitorRepository visitorRepository;
    private ArtworkRepository artworkRepository;
    @Autowired
    private service service;

    public CommentController(CommentRepository commentRepository, VisitorRepository visitorRepository, ArtworkRepository artworkRepository) {
        this.commentRepository = commentRepository;
        this.visitorRepository = visitorRepository;
        this.artworkRepository = artworkRepository;
        this.service = new service();
    }

    @PostMapping("/add")
    public ResponseEntity<?> postNewComment (@RequestBody CommentDTO commentDTO) {
        String currentEmail = loggedInUsers.get(BearerTokenUtil.getBearerTokenHeader());
        Visitor commentingVisitor = null;
        if (visitorRepository.existsByEmail(currentEmail)) {
            commentingVisitor = visitorRepository.findByEmail(currentEmail);
        } else {
            return new ResponseEntity<>("Korisnik ne postoji", HttpStatus.BAD_REQUEST);
        }
        Artwork artwork = artworkRepository.findById(commentDTO.getArtworkId()).get();
        Comment comment = new Comment(commentDTO.getContent(), commentingVisitor, artwork);
        commentRepository.save(comment);
        artwork.addComment(comment);
        return new ResponseEntity<>("Komentar uspjesno dodan!", HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeComment(@RequestBody CommentDTO commentDTO) {
        UUID commentId = commentDTO.getCommentId();
        if (commentRepository.existsById(commentId)) {
            Comment comment = commentRepository.findById(commentId).get();
            comment.getArtwork().removeComment(comment);
            commentRepository.deleteById(commentId);
            return ResponseEntity.ok().body("Comment successfully deleted.");
        } else {
            return new ResponseEntity<>("Komentar s tim ID-om ne postoji", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> getCommentsForArtwork(@RequestParam("id") UUID artworkId) {
       if (artworkRepository.existsById(artworkId)) {
           System.out.println("\n\n\nPrimljen zahtjev za dobiti komentare.");
           //return new ResponseEntity<>(new ArrayList<>(artworkRepository.findById(artworkId).get().getComments()), HttpStatus.OK);
           List<Comment> commentList = artworkRepository.findById(artworkId).get().getComments();
           System.out.println("Velicina comment seta je: " + commentList.size());
           return service.produceComments(commentList);
       } else {
           return new ResponseEntity<>("Djelo s tim ID-om ne postoji", HttpStatus.BAD_REQUEST);
       }
    }
}
