package service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.challenge.client.ApiWebClient;
import org.challenge.domain.BoardElement;
import org.challenge.domain.Bug;
import org.challenge.domain.Issue;
import org.challenge.domain.Label;
import org.challenge.domain.Task;
import org.challenge.service.BoardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest(classes = BoardService.class)
public class BoardServiceTest {

    private static Issue issue = new Issue();
    private static Bug bug = new Bug();
    private static Task task = new Task();
    @Autowired
    BoardService boardService;
    @MockBean
    ApiWebClient client;

    @BeforeAll
    static void setup() {
        BoardElement.labels = new HashMap<>();
        BoardElement.labels.put("Maintenance", new Label("id1","Test","blue"));
        BoardElement.labels.put("Research", new Label("id2","Bug","red"));
        BoardElement.labels.put("Test", new Label("id3","Research","green"));

        issue.setTitle("issue title test");
        issue.setDescription("issue description test");
        issue.setType("issue");

        bug.setTitle("bug title test");
        bug.setDescription("bug description test");
        bug.setType("bug");

        task.setTitle("task title test");
        task.setLabel(new Label("id1","Test","blue"));
        task.setType("task");
        task.setCategory("Test");
    }

    @Test
    public void testCreateIssue() {
        doNothing().when(client).createIssue(issue,"list id");
        boardService.createIssue(issue,"list id");

        verify(client, Mockito.times(1)).createIssue(issue,"list id");
    }

    @Test
    public void testCreateBug() {
        ArrayList members = new ArrayList();
        members.add("member1");
        members.add("member2");
        members.add("member3");
        when(client.getMembers("board id")).thenReturn(members);
        boardService.createBug(bug,"list id","board id");

        doNothing().when(client).createBug(bug,"list id");
        verify(client, Mockito.times(1)).getMembers("board id");
        verify(client, Mockito.times(1)).createBug(bug, "list id");
    }

    @Test
    public void testCreateTask() {
        ArrayList<String> members = new ArrayList();
        members.add("member1");
        members.add("member2");
        members.add("member3");
        when(client.getMembers("board id")).thenReturn(members);
        when(client.getLabels("board id")).thenReturn(BoardElement.labels);

        doNothing().when(client).createTask(task, "list id", task.getLabel().getName());
        boardService.createTask(task,"list id","board id");

        verify(client, Mockito.times(1)).getMembers("board id");
        verify(client, Mockito.times(1)).getLabels("board id");

        verify(client, Mockito.times(1)).createTask(task, "list id", BoardElement.labels.get(task.getLabel().getName()).getId());
    }

    @Test
    public void testCreateBoard() {
        when(client.createBoard("board name test","organization id")).thenReturn("board id test");
        boardService.createBoard("board name test","organization id");

        verify(client, Mockito.times(1)).createBoard("board name test","organization id");
    }

    @Test
    public void testCreateList() {
        when(client.createList("list name test", "board id test")).thenReturn("list id test");
        boardService.createList("list name test", "board id test");

        verify(client, Mockito.times(1)).createList("list name test", "board id test");
    }

    @Test
    public void testCreateLabel() {
        when(client.createLabel("label name test", "board id test", "skyblue")).thenReturn("label id test");
        boardService.createLabel("label name test", "board id test", "skyblue");

        verify(client, Mockito.times(1)).createLabel("label name test", "board id test", "skyblue");
    }
}
