package ch.iceage.demo.todolist.controller;

import ch.iceage.demo.todolist.domain.TodoItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;


@RequestMapping(value = "/todos",
        produces = "application/json")
public interface TodoItemController {
    @GetMapping(value = "/all")
    ResponseEntity<List<TodoItem>> getAllItems();

    @GetMapping(value = "/todo/{id}")
    ResponseEntity<TodoItem> getById(@PathVariable Long id);

    @GetMapping(value = "/not-done")
    ResponseEntity<List<TodoItem>> getAllDone();

    @GetMapping(value = "/done")
    ResponseEntity<List<TodoItem>> getAllNotDone();

    @PutMapping(value = "/todo",
            consumes = "application/json")
    ResponseEntity<TodoItem> add(@RequestBody TodoItem todoItem) throws URISyntaxException;

    @DeleteMapping(value = "/todo/{id}")
    ResponseEntity<Void> remove(@PathVariable Long id);

    @PostMapping(value = "/todo",
            consumes = "application/json")
    ResponseEntity<TodoItem> update(@RequestBody TodoItem todoItem);
}
