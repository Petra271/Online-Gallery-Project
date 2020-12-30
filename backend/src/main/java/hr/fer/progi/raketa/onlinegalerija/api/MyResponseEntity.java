package hr.fer.progi.raketa.onlinegalerija.api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class MyResponseEntity<T> extends ResponseEntity<T>{
    public MyResponseEntity(HttpStatus status) {
        super(status);
    }

    public MyResponseEntity(T body, HttpStatus status) {
        super(body, status);
    }

    public MyResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public MyResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }
}
