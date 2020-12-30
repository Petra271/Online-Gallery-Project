package hr.fer.progi.raketa.onlinegalerija.dao;

import com.sun.istack.NotNull;

public class ArtworkDTO {
    @NotNull
    private String collectionName;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String style;

    @NotNull
    private String price;

    public String getPrice() {
        return price;
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

    public String getStyle() {
        return style;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
