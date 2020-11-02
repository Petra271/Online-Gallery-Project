package hr.fer.progi.raketa.onlinegalerija.model;

import java.util.UUID;

public class Admin extends User{

    public Admin(UUID id, String name, String surname, String email,
                 String password, String paypalMail, String paypalPass) {
        super(name, surname, email, password, paypalMail, paypalPass);
    }


}
