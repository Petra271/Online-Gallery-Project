package hr.fer.progi.raketa.onlinegalerija.api;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BearerTokenUtil {

    public static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7);
    }
}
