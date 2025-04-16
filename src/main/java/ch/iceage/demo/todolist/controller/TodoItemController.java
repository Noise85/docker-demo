package ch.iceage.demo.todolist.controller;

import ch.iceage.demo.todolist.domain.TodoItem;
import ch.iceage.demo.todolist.domain.dto.TaskSearchDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;


@RequestMapping(value = "/todos",
        produces = "application/json")
public interface TodoItemController {
    @GetMapping(value = "/all")
    ResponseEntity<Iterable<TodoItem>> getAllItems(@PageableDefault Pageable page);

    @PostMapping(value = "/all",
            consumes = "application/json")
    ResponseEntity<Iterable<TodoItem>> getAllBy(@RequestBody TaskSearchDTO taskSearchDTO);

    @GetMapping(value = "/todo/{id}")
    ResponseEntity<TodoItem> getById(@PathVariable Long id);

    @GetMapping(value = "/todo/status/{status}")
    ResponseEntity<Iterable<TodoItem>> getByStatus(@PathVariable String status, @PageableDefault Pageable page);

    @GetMapping(value = "/board")
    ResponseEntity<Iterable<TodoItem>> getBoardItems(@PageableDefault Pageable page);

    @GetMapping(value = "/backlog")
    ResponseEntity<Iterable<TodoItem>> getBacklogItems(@PageableDefault Pageable page);

    @PutMapping(value = "/todo",
            consumes = "application/json")
    ResponseEntity<TodoItem> add(@RequestBody TodoItem todoItem) throws URISyntaxException;

    @DeleteMapping(value = "/todo/{id}")
    ResponseEntity<Void> remove(@PathVariable Long id);

    @PostMapping(value = "/todo",
            consumes = "application/json")
    ResponseEntity<TodoItem> update(@RequestBody TodoItem todoItem);
}
