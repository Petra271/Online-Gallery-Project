package hr.fer.progi.raketa.onlinegalerija.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.raketa.onlinegalerija.model.Artwork;
import com.fasterxml.jackson.databind.util.JSONPObject;
import hr.fer.progi.raketa.onlinegalerija.model.Artwork;
import hr.fer.progi.raketa.onlinegalerija.model.Collection;
import hr.fer.progi.raketa.onlinegalerija.model.Comment;
import hr.fer.progi.raketa.onlinegalerija.model.Transaction;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONException;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class service {
    public ResponseEntity<Map<String, Map<String, byte[]>>> produceCollections(Set<Collection> collections) throws JSONException, JsonProcessingException {
        Map<String, Map<String, byte[]>> retMap = new HashMap<>();

        for(Collection c : collections){
            Map<String, byte[]> collMap = new HashMap<>();
            for(Artwork a : c.getArtworks())
                collMap.put(artToMapJson(a), a.getImageInBytes());

            retMap.put(collToMapJson(c), collMap);
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Map<String, byte[]>>> produceCollectionsSingles(Set<Collection> collections) throws JSONException, JsonProcessingException {
        Map<String, Map<String, byte[]>> retMap = new HashMap<>();

        for(Collection c : collections){
            Map<String, byte[]> collMap = new HashMap<>();

            if(c.getArtworks().iterator().hasNext()) {
                Artwork a = c.getArtworks().iterator().next();
                collMap.put(artToMapJson(a), a.getImageInBytes());
            }

            retMap.put(collToMapJson(c), collMap);
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> produceCollectionsList(Set<Collection> collections) throws JSONException{
        Map<String, String> collectionList = new HashMap<>();

        for(Collection c : collections)
            collectionList.put(c.getName(), produceCollectionJson(c));

        return new ResponseEntity<>(collectionList, HttpStatus.OK);
    }

    public ResponseEntity<Map<UUID, String>> produceComments(Set<Comment> commentSet) {
        Map<UUID, String> commentMap = new HashMap<>();
        for (Comment c : commentSet) {
            commentMap.put(c.getCommentId(), produceCommentJson(c));
        }
        System.out.println("Velicin ampae je : " + commentMap.size());
        return new ResponseEntity<>(commentMap, HttpStatus.OK);
    }

    public ResponseEntity<List<String>> produceTransactions(List<Transaction> transactions) {
        List<String> transactionJsonList = new ArrayList<>();
        for (Transaction t : transactions) {
            transactionJsonList.add(produceTransactionJson(t));
        }
        System.out.println("Velicina liste je: " + transactionJsonList.size());
        return new ResponseEntity<>(transactionJsonList, HttpStatus.OK);
    }

    private String produceCollectionJson(Collection c) throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\": \"").append(c.getName()).append("\",");
        sb.append("\"description\": \"").append(c.getDescription()).append("\",");
        sb.append("\"style\": \"").append(c.getStyle().toString()).append("\"");
        sb.append("}");
        

        System.out.println(sb.toString());
        return sb.toString();
    }

    private String collToMapJson(Collection collection) throws JsonProcessingException {
        Map<String, String> res = new HashMap<>();
        res.put("Author", collection.getArtist().getName());
        res.put("Name", collection.getName());
        res.put("Description", collection.getDescription());
        res.put("Style", collection.getStyle().toString());
        res.put("Number of artworks", String.valueOf(collection.getArtworks().size()));
        return new ObjectMapper().writeValueAsString(res);
    }

    private String artToMapJson(Artwork artwork) throws JsonProcessingException {
        Map<String, String> res = new HashMap<>();
        res.put("Name", artwork.getName());
        res.put("Description", artwork.getDescription());
        res.put("Style", artwork.getStyle().toString());
        return new ObjectMapper().writeValueAsString(res);
    }

    private String produceArtworkJson(Artwork a) throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\": \"").append(a.getId()).append("\",");
        sb.append("\"name\": \"").append(a.getName()).append("\",");
        sb.append("\"description\": \"").append(a.getDescription()).append("\",");
        sb.append("\"style\": \"").append(a.getStyle().toString()).append("\",");
        sb.append("\"price\": \"").append(a.getPrice()).append("\"");
        sb.append("}");
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String produceCommentJson(Comment c) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\": \"").append(c.getVisitor().getName()).append("\",");
        sb.append("\"surname\": \"").append(c.getVisitor().getSurname()).append("\",");
        sb.append("\"content\": \"").append(c.getContent());
        sb.append("}");
        return sb.toString();
    }

    private String produceTransactionJson(Transaction t) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"payerName\": \"").append(t.getPayer().getName()).append("\",");
        sb.append("\"payerSurname\": \"").append(t.getPayer().getSurname()).append("\",");
        sb.append("\"receiverName\": \"").append(t.getReceiver().getName()).append("\",");
        sb.append("\"receiverSurname\": \"").append(t.getReceiver().getSurname()).append("\",");
        sb.append("\"amountToArtist\": \"").append(t.getAmountToArtist()).append("\",");
        sb.append("\"provisionAmount\": \"").append(t.getProvisionAmount());
        sb.append("}");
        return sb.toString();
    }


}
