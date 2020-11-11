package hr.fer.progi.raketa.onlinegalerija.model;

import java.util.UUID;

public class Admin extends Visitor {

    public Admin(UUID id, String name, String surname, String email,
                 String password, String paypalMail) {
        super(name, surname, email, password, paypalMail);
    }


}
