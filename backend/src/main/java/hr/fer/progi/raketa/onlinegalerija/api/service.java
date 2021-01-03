package hr.fer.progi.raketa.onlinegalerija.api;

import com.fasterxml.jackson.databind.util.JSONPObject;
import hr.fer.progi.raketa.onlinegalerija.model.Artwork;
import hr.fer.progi.raketa.onlinegalerija.model.Collection;
import hr.fer.progi.raketa.onlinegalerija.model.Comment;
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
    public ResponseEntity<Map<JSONObject, Map<JSONObject, byte[]>>> produceCollections(Set<Collection> collections) throws JSONException {
        Map<JSONObject, Map<JSONObject, byte[]>> retMap = new HashMap<>();

        for(Collection c : collections){
            Map<JSONObject, byte[]> collMap = new HashMap<>();
            for(Artwork a : c.getArtworks())
                collMap.put(produceArtworkJson(a), a.getImageInBytes());

            retMap.put(produceCollectionJson(c), collMap);
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<UUID, String>> produceComments(Set<Comment> commentSet) {
        Map<UUID, String> commentMap = new HashMap<>();
        for (Comment c : commentSet) {
            commentMap.put(c.getCommentId(), produceCommentJson(c));
        }
        System.out.println("Velicin ampae je : " + commentMap.size());
        return new ResponseEntity<>(commentMap, HttpStatus.OK);
    }

    private JSONObject produceCollectionJson(Collection c) throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\": \"").append(c.getName()).append("\",");
        sb.append("\"description\": \"").append(c.getDescription()).append("\",");
        sb.append("\"style\": \"").append(c.getStyle().toString()).append("\"");
        sb.append("}");
        JSONObject json = new JSONObject(sb.toString());

        System.out.println(json);
        return json;
    }

    private JSONObject produceArtworkJson(Artwork a) throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\": \"").append(a.getId()).append("\",");
        sb.append("\"name\": \"").append(a.getName()).append("\",");
        sb.append("\"description\": \"").append(a.getDescription()).append("\",");
        sb.append("\"style\": \"").append(a.getStyle().toString()).append("\",");
        sb.append("\"price\": \"").append(a.getPrice()).append("\"");
        sb.append("}");
        System.out.println(sb.toString());
        return new JSONObject(sb.toString());
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


}
