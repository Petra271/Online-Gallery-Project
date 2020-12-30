package hr.fer.progi.raketa.onlinegalerija.dao;

import com.sun.istack.NotNull;

import java.util.UUID;

public class ArtworkIdDTO {

    @NotNull
    private UUID artworkId;

    public UUID getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(UUID artworkId) {
        this.artworkId = artworkId;
    }
}
