package hr.fer.progi.raketa.onlinegalerija.api;

import hr.fer.progi.raketa.onlinegalerija.model.Artwork;
import hr.fer.progi.raketa.onlinegalerija.model.Collection;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class service {
    public ResponseEntity<Map<String, Map<String, byte[]>>> produceCollections(List<Collection> collections){
        Map<String, Map<String, byte[]>> retMap = new HashMap<>();

        for(Collection c : collections){
            Map<String, byte[]> collMap = new HashMap<>();
            for(Artwork a : c.getArtworks())
                collMap.put(produceArtworkJson(a), a.getImageInBytes());

            retMap.put(produceCollectionJson(c), collMap);
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Map<String, String>>> testMapReturn(){
        Map<String, Map<String, String>> map = new HashMap<>();
        Map<String, String> subMap = new HashMap<>();
        subMap.put("SubKey", "subValue");
        map.put("testKey", subMap);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private String produceCollectionJson(Collection c){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\": \"").append(c.getName()).append("\",");
        sb.append("\"description\": \"").append(c.getDescription()).append("\",");
        sb.append("\"style\": \"").append(c.getStyle().toString()).append("\"");
        sb.append("}");
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String produceArtworkJson(Artwork a){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\": \"").append(a.getName()).append("\",");
        sb.append("\"description\": \"").append(a.getDescription()).append("\",");
        sb.append("\"style\": \"").append(a.getStyle().toString()).append("\",");
        sb.append("\"price\": \"").append(a.getPrice()).append("\"");
        sb.append("}");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
