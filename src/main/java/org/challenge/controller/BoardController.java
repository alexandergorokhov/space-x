package org.challenge.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.challenge.domain.Bug;
import org.challenge.domain.Issue;
import org.challenge.domain.Task;
import org.challenge.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService service) {
        this.boardService = service;
    }
    /** ex: http://localhost:3000/api/v1/board/issue?listId=list1&boardId=board1
     * body: {
     * "type": "issue",
     * "title": "title issue",
     * "description": "description issue",
     * }
     * @param issue to be created
     * @param listId list id
     * @return
     * <201>Issue Created</201>
     * <400>Bad Request</400>
     * <500>Internal Error</500>
     */
    @ApiOperation(value = "Post issue", notes = "Creates issue on the provided list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Issue created"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Not able to create issue")
    })
    @PostMapping(value = "/board/issue", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createIssue(@RequestBody Issue issue, @RequestParam String listId) {
        if (issue == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request");
        }
        try {
            if (issue.getType().equals("issue")) {
                boardService.createIssue(issue, listId);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid type");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to create issue");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body("issue created");
    }
    /** ex: http://localhost:3000/api/v1/board/bug?listId=list1&boardId=board1
     * body: {
     * "type": "bug",
     * "description": "bug description",
     * }
     * @param bug bug to be created
     * @param listId list id
     * @param boardId board id
     * @return
     * <201>Bug Created</201>
     * <400>Bad Request</400>
     * <500>Internal Error</500>
     */
    @ApiOperation(value = "Post bug", notes = "Creates bug on the provided board and list")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Bug created"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Not able to create bug")
    })
    @PostMapping(value = "/board/bug", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createBug(@RequestBody Bug bug, @RequestParam String listId,
        @RequestParam String boardId) {
        if (bug == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request");
        }
        try {
            if (bug.getType().equals("bug")) {
                boardService.createBug(bug, listId, boardId);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid type");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to create bug");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body("bug created");
    }

    /** ex: http://localhost:3000/api/v1/board/task?listId=list1&boardId=board1
     * body: {
     * "type": "task",
     * "title": "task1",
     * "category": "todo",
     * }
     * @param task task to be created
     * @param listId list id
     * @param boardId board id
     * @return
     * <201>Task Created</201>
     * <400>Bad Request</400>
     * <500>Internal Error</500>
     */
    @ApiOperation(value = "Post task by list id, board id", notes = "Creates a task on the provided board and list")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Task created"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Not able to create task")
    })
    @PostMapping(value = "/board/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTask(@RequestBody Task task, @RequestParam String listId, @RequestParam String boardId) {
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid request");
        }
        try {
            if (task.getType().equals("task")) {
                boardService.createTask(task, listId, boardId);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid type");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to create task");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body("task created");
    }

    /**
     * ex: http://localhost:3000/api/v1/board?boardName=board1
     * @param boardName name of the board
     * @return
     * <201>Board created: board id<201/>
     * <400>Bad Request<400/>
     * <500>Internal Error <500/>
     * @throws RuntimeException
     */
    @ApiOperation(value = "Post board", notes = "Creates board")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Board created. Returns board id"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Not able to create board")
    })
    @PostMapping(value = "/board", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createBoard(@RequestParam(name = "boardName") String boardName) {
        String boardId = null;
        try {
            boardId = boardService.createBoard(boardName);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to create board");

        }
        return new ResponseEntity<>("Board created: " + boardId, HttpStatus.CREATED);
    }

    /**
     * ex: http://localhost:3000/api/v1/list?listName=list1&boardId=board1
     * @param listName name of the list
     * @param boardId id of the bard
     * @return
     * <201>List created: list id<201/>
     * <400>Bad Request<400/>
     * <500>Internal Error <500/>
     * @throws RuntimeException
     */
    @ApiOperation(value = "Post list", notes = "Creates list on board")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "List created. Returns list id"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Not able to create list")
    })
    @PostMapping(value = "/list", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createList(@RequestParam(name = "listName") String listName, @RequestParam(name = "boardId") String boardId) {
        String listId;
        try {
            listId = boardService.createList(listName, boardId);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to create board");

        }
        return new ResponseEntity<>("List id: " + listId, HttpStatus.CREATED);
    }

    /**
     * ex: http://localhost:3000/api/v1/label?labelName=label1&labelColor=red&boardId=board1
     * @param labelName name of the label
     * @param color color of the label
     * @param boardId id of the bard
     * @return
     * <201>Label created: label id<201/>
     * <400>Bad Request<400/>
     * <500>Internal Error <500/>
     * @throws RuntimeException
     */
    @ApiOperation(value = "Post label", notes = "Creates label on board")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Label created. Returns label id"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Not able to create label")
    })
    @PostMapping(value = "/label",  produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createLabel(@RequestParam(name = "labelName") String labelName, @RequestParam(name = "labelColor") String color, @RequestParam(name = "boardId") String boardId) {
        String idLabel;
        try {
            idLabel = boardService.createLabel(labelName, color, boardId);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to create label");

        }
        return new ResponseEntity<>("idLabel: " + idLabel, HttpStatus.CREATED);
    }

}
