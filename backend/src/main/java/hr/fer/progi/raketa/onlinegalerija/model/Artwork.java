//package hr.fer.progi.raketa.onlinegalerija.model;
//
//import javax.imageio.ImageIO;
//import javax.persistence.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
//import java.io.File;
//import java.util.UUID;
//
//@Entity
//@Table(name="artwork")
//public class Artwork {
//    @Id
//    private final UUID id;
//    private String name;
//    private String description;
//    private Style style;
//    private double price;
//    private BufferedImage image;
//    @ManyToOne(cascade = CascadeType.PERSIST)
//    private Collection collection;
//
//    public Artwork(UUID id, String name, String description, Style style, double price, String dest) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.style = style;
//        this.price = price;
//        try {
//            this.image = ImageIO.read(new File(dest));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public BufferedImage getImage() {
//        return image;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Style getStyle() {
//        return style;
//    }
//
//    public void setStyle(Style style) {
//        this.style = style;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//    public void setImage(String dest){
//        try {
//            this.image = ImageIO.read(new File(dest));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
