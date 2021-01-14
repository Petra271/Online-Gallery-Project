package hr.fer.progi.raketa.onlinegalerija.dao;

public class ApplicationDTO {
    private String contestName;

    private String[] collections;

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String[] getCollections() {
        return collections;
    }

    public void setCollections(String[] collections) {
        this.collections = collections;
    }
}
