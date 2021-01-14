package hr.fer.progi.raketa.onlinegalerija.dao;

import com.sun.istack.NotNull;

public class ContestDTO {
    @NotNull
    private String beginDateTime;

    @NotNull
    private String duration;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String style;

    @NotNull
    private double provision;

    public double getProvision() {
        return provision;
    }

    public void setProvision(double provision) {
        this.provision = provision;
    }

    public String getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(String beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public void setStyle(String style) {
        this.style = style;
    }
}
