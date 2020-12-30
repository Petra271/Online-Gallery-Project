package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="comment")
public class Comment {


    @Id
    @Column(name="comment_id", nullable = false)
    private UUID commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artwork", nullable = false)
    private Artwork artwork;

    /**
     * Content of the comment.
     */
    @Column(name="content", nullable = false)
    private String content;

    /**
     * Visitor who made the comment.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visitor", nullable = false)
    private Visitor visitor;


    public Comment() {

    }

    public Comment(String content, Visitor visitor, Artwork artwork) {
        this.content = content;
        this.visitor = visitor;
        this.artwork = artwork;
        this.commentId = UUID.randomUUID();
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public String getContent() {
        return content;
    }
}
