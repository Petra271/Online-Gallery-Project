package hr.fer.progi.raketa.onlinegalerija.model;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="contest")
public class Contest {
    @Id
    @Column(name ="id", nullable = false)
    private final UUID id;

    @Column(name="beginDateTime", nullable = false)
    private LocalDateTime beginDateTime;

    @Column(name="duration", nullable = false)
    private Duration duration;

    @Column(name="workingName", nullable = false)
    private String workingName;

    @Column(name="style", nullable = false)
    private Style style;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="provision", nullable = false)
    private double provision;

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    private Set<ContestApplication> applications;

    public Contest() {
        this.id = UUID.randomUUID();
    }

    public Contest(LocalDateTime beginDateTime, Duration duration, String workingName, Style style, String description, double provision) {
        this.id = UUID.randomUUID();
        this.beginDateTime = beginDateTime;
        this.duration = duration;
        this.workingName = workingName;
        this.style = style;
        this.description = description;
        this.provision = provision;
        this.applications = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(LocalDateTime beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getWorkingName() {
        return workingName;
    }

    public void setWorkingName(String workingName) {
        this.workingName = workingName;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getProvision() {
        return provision;
    }

    public void setProvision(double provision) {
        this.provision = provision;
    }

    public Set<ContestApplication> getApplications() {
        return applications;
    }

    public void setApplications(Set<ContestApplication> applications) {
        this.applications = applications;
    }

    public void addApplication(ContestApplication ca){
        applications.add(ca);
        ca.setContest(this);
    }
}
