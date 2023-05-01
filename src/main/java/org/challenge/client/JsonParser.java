package org.challenge.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.challenge.domain.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class JsonParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);


    public String parseField(String json, String field) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json).get(field).asText();

        } catch (JsonProcessingException e) {
            logger.error("Error parsing json");
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> parseArray(String json, String field) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            ArrayList<LinkedHashMap> arrayList = mapper.readValue(json,ArrayList.class);
            ArrayList<String> list = new ArrayList<>();
                for (final LinkedHashMap objNode : arrayList) {
                    list.add(objNode.get(field).toString());
                }

            return list;

        } catch (JsonProcessingException e) {
            logger.error("Error parsing json");
            throw new RuntimeException(e);
        }
    }

    public HashMap<String,Label> parseLabels(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ArrayList<LinkedHashMap> arrayList = mapper.readValue(json,ArrayList.class);
            HashMap<String,Label> map = new HashMap<>();
            for (final LinkedHashMap objNode : arrayList) {
                if(objNode.get("name").toString().isEmpty()){
                    continue;
                }
                Label label = new Label();
                label.setId(objNode.get("id").toString());
                label.setName(objNode.get("name").toString());
                label.setColor(objNode.get("color").toString());
                map.put(label.getName(),label);
            }

            return map;

        } catch (JsonProcessingException e) {
            logger.error("Error parsing json");
            throw new RuntimeException(e);
        }
    }
}
