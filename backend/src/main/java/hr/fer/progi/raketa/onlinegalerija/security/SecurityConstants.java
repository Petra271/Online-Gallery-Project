package hr.fer.progi.raketa.onlinegalerija.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/visitor/registration";
    public static final String LOGOUT = "/visitor/logout";
    public static final String COLLECTION_CREATION = "/artist/createCollection";
    public static final String ADD_ARTWORK = "/artist/addArtwork";
    public static final String GET_COLLECTIONS = "/artist/getCollections";
    public static final String TEST = "/admin/test";
    public static final String ADD_COMMENT ="/comment/add";
    public static final String GET_COMMENTS="/comment/get";
    public static final String DELETE_COMMENT="/comment/remove";
    public static final String ADD_CONTEST="/admin/createContest";
    public static final String APPLY_TO_CONTEST="/artist/applyToContest";
    public static final String ADD_EXHIBITION="/admin/createExhibition";

    public static final String GET_EXHIBITION="/admin/getExhibition";
    public static final String ADD_TRANSACTION="/transaction/addTransaction";
    public static final String GET_ALL_TRANSACTIONS="/transaction/getAllTransactions";
    public static final String GET_ALL_TRANSACTIONS_BY_USER="/transaction/getAllTransactionsByUser";
}
