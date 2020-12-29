package hr.fer.progi.raketa.onlinegalerija.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/visitor/registration";
    public static final String COLLECTION_CREATION = "/visitor/createCollection";
    public static final String TEST = "/visitor/test";
}
