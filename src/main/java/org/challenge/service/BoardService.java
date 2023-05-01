package org.challenge.service;

import org.challenge.client.ApiWebClient;
import org.challenge.domain.BoardElement;
import org.challenge.domain.Bug;
import org.challenge.domain.Issue;
import org.challenge.domain.Label;
import org.challenge.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Service
public class BoardService {

    private final ApiWebClient apiWebClient;

    @Autowired
    public BoardService(ApiWebClient apiWebClient) {
        this.apiWebClient = apiWebClient;
    }

    public void createIssue(Issue issue, String listId) {
        apiWebClient.createIssue(issue, listId);
    }

    public void createBug(Bug bug, String listId, String boardId) {
        bug.setTitle(buildBugTitle());
        var members = apiWebClient.getMembers(boardId);
        bug.setAssignee(members.get(new Random().nextInt(members.size())));
        apiWebClient.createBug(bug, listId);
    }

    public void createTask(Task task, String listId, String boardId) {
        var members = apiWebClient.getMembers(boardId);
        task.setAssignee(members.get(new Random().nextInt(members.size())));
        if (BoardElement.labels.get(task.getLabel()) == null) {
            HashMap<String, Label> labels = apiWebClient.getLabels(boardId);
            BoardElement.labels = labels;
        }
        Label label = BoardElement.labels.get(task.getCategory());
        apiWebClient.createTask(task, listId, label.getId());
    }

    public String createBoard(String boardName) {
        return apiWebClient.createBoard(boardName);
    }

    public String createList(String listName, String boardId) {
        return apiWebClient.createList(listName, boardId);
    }

    public String createLabel(String labelName, String color, String boardId) {
        var labelId = apiWebClient.createLabel(labelName, color, boardId);
        BoardElement.labels.put(labelName, new Label(labelId, labelName, color));
        return labelId;
    }

    private String buildBugTitle() {
        var sb = new StringBuilder();
        return sb.append("bug-").append("-").append(createRandomSequence(5)).append("-").append(new Random().nextInt()).toString();
    }

    private StringBuilder createRandomSequence(int len) {
        var name = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int v = 1 + (int) (Math.random() * 26);
            char c = (char) (v + (i == 0 ? 'A' : 'a') - 1);
            name.append(c);
        }
        return name;
    }
}
