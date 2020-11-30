package hr.fer.progi.raketa.onlinegalerija.dao;

import org.springframework.web.bind.annotation.PostMapping;

public interface Dao {
    boolean register(String name, String surname, String email, String password, String paypalMail);
    boolean login (String email, String password);
}
