package hr.fer.progi.raketa.onlinegalerija.model;

import java.util.ArrayList;

public class Collection {
    private String name;
    private String description;
    private Style style;
    private ArrayList<Artwork> artworks = new ArrayList<>();

    public Collection(String name, String description, Style style) {
        this.name = name;
        this.description = description;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
