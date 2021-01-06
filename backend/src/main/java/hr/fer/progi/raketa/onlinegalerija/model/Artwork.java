package hr.fer.progi.raketa.onlinegalerija.model;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.File;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="artwork")
public class Artwork {
    @Id
    @Column(name ="id", nullable = false)
    private final UUID id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="style", nullable = false)
    private Style style;

    @Column(name="price", nullable = false)
    private double price;

    @Column(name="image", nullable = false)
    private byte[] imageInBytes;
    //private BufferedImage image;

    @Column(name="fileType", nullable = false)
    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="collection_id", nullable = false)
    private Collection collection;

    @OneToMany(mappedBy = "artwork", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Artwork(){
        this.id = UUID.randomUUID();
    }

    public Artwork(String name, String description, Style style, double price,
                   byte[] imageInBytes, String fileType, Collection collection) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.style = style;
        this.price = price;
//        try {
//            this.imageInBytes = ImageIO.read(new ByteArrayInputStream(imageInBytes));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.imageInBytes = imageInBytes;
        this.fileType = fileType;
        this.collection = collection;
    }

    public UUID getId() {
        return id;
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

//    public BufferedImage getImage() {
//        return image;
//    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
//    public void setImage(byte[] imageInBytes){
//        try {
//            this.image = ImageIO.read(new ByteArrayInputStream(imageInBytes));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public byte[] getImageInBytes() {
        return imageInBytes;
    }

    public void setImageInBytes(byte[] imageInBytes) {
        this.imageInBytes = imageInBytes;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
