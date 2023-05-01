package controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.challenge.controller.BoardController;
import org.challenge.domain.BoardElement;
import org.challenge.domain.Bug;
import org.challenge.domain.Issue;
import org.challenge.domain.Label;
import org.challenge.domain.Task;
import org.challenge.service.BoardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.HashMap;

@SpringBootTest(classes = BoardController.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @MockBean
    BoardService boardService;

    @Autowired
    BoardController boardController;

    private static Issue issue = new Issue();
    private static Bug bug = new Bug();
    private static Task task = new Task();

    @BeforeAll
    static void setup() {
        BoardElement.labels = new HashMap<>();
        BoardElement.labels.put("Maintenance", new Label());
        BoardElement.labels.put("Research", new Label());
        BoardElement.labels.put("Test", new Label());

        issue.setTitle("issue title test");
        issue.setDescription("issue description test");
        issue.setType("issue");

        bug.setTitle("bug title test");
        bug.setDescription("bug description test");
        bug.setType("bug");

        task.setTitle("task title test");
        task.setLabel(new Label());
        task.setType("task");
    }

    @Test
    public void testCreateIssue() throws Exception {
        doNothing().when(boardService).createIssue(issue, "list id");

        ResponseEntity response = boardController.createIssue(issue,"list id");
        Assert.isTrue(response.getStatusCode().is2xxSuccessful(), "Status code is not 2xx");
    }

    @Test
    public void testCreateBug(){
        doNothing().when(boardService).createBug(bug,"list id","board id");

        ResponseEntity response = boardController.createBug(bug,"list id","board id");
        Assert.isTrue(response.getStatusCode().is2xxSuccessful(), "Status code is not 2xx");
    }

    @Test
    public void testCreateTask(){
        doNothing().when(boardService).createTask(task,"list id","board id");

        ResponseEntity response = boardController.createTask(task,"list id","board id");
        Assert.isTrue(response.getStatusCode().is2xxSuccessful(), "Status code is not 2xx");
    }

    @Test
    public void testCreateTaskException(){
        doThrow( new RuntimeException("test Exception")).when(boardService).createTask(task,"list id","board id");

        ResponseEntity response = boardController.createTask(task, "list id","board id");
        Assert.isTrue(response.getStatusCode().is5xxServerError(), "Status code is not 5xx");
    }

    @Test
    public void testCreateTaskBadRequest(){
        ResponseEntity response = boardController.createIssue(null,"list id");
        Assert.isTrue(response.getStatusCode().is4xxClientError(), "Status code is not 4xx");
    }


}
