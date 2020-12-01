package hr.fer.progi.raketa.onlinegalerija.service;

import hr.fer.progi.raketa.onlinegalerija.model.Visitor;
import hr.fer.progi.raketa.onlinegalerija.repository.VisitorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static java.util.Collections.emptyList;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private VisitorRepository visitorRepository;

    public UserDetailsServiceImpl(VisitorRepository visitorRepository){
        this.visitorRepository = visitorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Visitor visitor = visitorRepository.findByEmail(email);
        if(visitor == null){
            throw new UsernameNotFoundException(email);
        }
        return new User(visitor.getEmail(), visitor.getPassword(), emptyList());
    }
}
