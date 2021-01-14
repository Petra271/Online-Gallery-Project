package hr.fer.progi.raketa.onlinegalerija.dao;

import com.sun.istack.NotNull;
import hr.fer.progi.raketa.onlinegalerija.model.Style;

public class CollectionDTO {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String style;

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

    public void setStyle(String style) {
        this.style = style;
    }
}
