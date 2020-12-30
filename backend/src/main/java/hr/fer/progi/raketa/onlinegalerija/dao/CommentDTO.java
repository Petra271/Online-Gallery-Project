package hr.fer.progi.raketa.onlinegalerija.dao;

import com.sun.istack.NotNull;
import hr.fer.progi.raketa.onlinegalerija.model.Artwork;
import hr.fer.progi.raketa.onlinegalerija.model.Visitor;

import java.util.UUID;

public class CommentDTO {

    private String content;


    private UUID artworkId;

    private UUID commentId;

    public UUID getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public UUID getArtworkId() {
        return artworkId;
    }


}
