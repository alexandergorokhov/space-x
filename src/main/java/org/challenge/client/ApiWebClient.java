package org.challenge.client;


import org.challenge.domain.Bug;
import org.challenge.domain.Issue;
import org.challenge.domain.Label;
import org.challenge.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class ApiWebClient {
    private WebClient client;
    private JsonParser jsonParser;
    private static final Logger logger = LoggerFactory.getLogger(ApiWebClient.class);
    private  final String API_KEY;
    private  final String TOKEN;

    @Autowired
    public ApiWebClient(JsonParser jsonParser, @Value("${trello.api.key}") String API_KEY, @Value("${trello.api.token}") String TOKEN) {
        this.client = WebClient.builder()
            .baseUrl("https://api.trello.com/1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
        this.jsonParser = jsonParser;
        this.API_KEY= API_KEY;
        this.TOKEN = TOKEN;
    }


    public void createIssue(Issue issue, String idList) {
        logger.info("Creating issue");

        ResponseEntity result = client.post().uri(uriBuilder -> uriBuilder
                .path("/cards")
                .queryParam("idList", idList)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("desc", issue.getDescription())
                .queryParam("name", issue.getTitle())
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error creating issue");
            throw new RuntimeException("Error creating issue");
        }
        logger.info("Issue created");
    }

    public void createBug(Bug bug, String idList) {
        logger.info("Creating issue");

        ResponseEntity result = client.post().uri(uriBuilder -> uriBuilder
                .path("/cards")
                .queryParam("idList", idList)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("idLabels", bug.getLabelId())
                .queryParam("idMembers", bug.getAssignee())
                .queryParam("name", bug.getTitle())
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error creating bug");
            throw new RuntimeException("Error creating bug");
        }

        logger.info("Bug created");
    }

    public void createTask(Task task, String idList, String idLabel) {
        logger.info("Creating task");

        ResponseEntity result = client.post().uri(uriBuilder -> uriBuilder
                .path("/cards")
                .queryParam("idList", idList)
                .queryParam("idLabels", idLabel)
                .queryParam("idMembers", task.getAssignee())
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("name", task.getTitle())
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error creating task");
            throw new RuntimeException("Error creating task");
        }
        logger.info("Task created");
    }

    public String createBoard(String board) {
        logger.info("Creating board");

        ResponseEntity result = client.post().uri(uriBuilder -> uriBuilder
                .path("/boards")
                .queryParam("name", board)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error creating board");
            throw new RuntimeException("Error creating board");
        }
        logger.info("Board created");
        return jsonParser.parseField(result.getBody().toString(), "id");
    }

    public String createList(String list, String idBoard) {
        logger.info("Creating list");

        ResponseEntity result = client.post().uri(uriBuilder -> uriBuilder
                .path("/lists")
                .queryParam("name", list)
                .queryParam("idBoard", idBoard)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error creating board");
            throw new RuntimeException("Error creating board");
        }
        logger.info("List created");
        return jsonParser.parseField(result.getBody().toString(), "id");
    }


    public ArrayList<String> getMembers(String idBoard) {
        logger.info("Getting members");

        ResponseEntity result = client.get().uri(uriBuilder -> uriBuilder
                .path("/boards/" + idBoard + "/members")
                .queryParam("key", this.API_KEY.toString())
                .queryParam("token", this.TOKEN.toString())
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error getting members");
            throw new RuntimeException("Error getting members");
        }
        logger.info("Members retrieved");
        return jsonParser.parseArray(result.getBody().toString(), "id");
    }

    public String createLabel(String labelName, String color, String idBoard) {
        logger.info("Creating label");

        ResponseEntity result = client.post().uri(uriBuilder -> uriBuilder
                .path("/labels")
                .queryParam("name", labelName)
                .queryParam("color", color)
                .queryParam("idBoard", idBoard)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error creating label");
            throw new RuntimeException("Error creating label");
        }
        logger.info("Label created");
        return jsonParser.parseField(result.getBody().toString(), "id");
    }

    public HashMap<String, Label> getLabels(String idBoard) {
        logger.info("Getting labels");

        ResponseEntity result = client.get().uri(uriBuilder -> uriBuilder
                .path("/boards/" + idBoard + "/labels")
                .queryParam("idBoard", idBoard)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .build())
            .retrieve()
            .toEntity(String.class)
            .block();
        if (result.getStatusCode() != HttpStatus.OK) {
            logger.error("Error creating label");
            throw new RuntimeException("Error creating label");
        }
        logger.info("Label created");
        return jsonParser.parseLabels(result.getBody().toString());
    }
}
