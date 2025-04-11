package ch.iceage.demo.todolist.controller;

import ch.iceage.demo.todolist.domain.TodoItem;
import ch.iceage.demo.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 11:57:28
 */
@RestController
@RequestMapping(value = "/todos",
				produces = "application/json")
public class TodoItemControllerImpl implements TodoItemController {

	@Autowired
	private TodoService todoService;

	@Override
	@GetMapping(value = "/all")
	public ResponseEntity<List<TodoItem>> getAllItems() {
		List<TodoItem> todoItems = todoService.getAll();
		if(todoItems.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.ok(todoService.getAll());
		}
	}

	@Override
	@GetMapping(value = "/todo/{id}")
	public ResponseEntity<TodoItem> getById(@PathVariable Long id) {
		TodoItem todoItem = todoService.find(id);
		if(todoItem.getId() == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(todoItem);
		}
	}

	@Override
	@GetMapping(value = "/done")
	public ResponseEntity<List<TodoItem>> getAllDone() {
		List<TodoItem> todoItems = todoService.getDone();
		if(todoItems.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(todoItems);
		}
	}

	@Override
	@GetMapping(value = "/not-done")
	public ResponseEntity<List<TodoItem>> getAllNotDone() {
		List<TodoItem> todoItems = todoService.getNotDone();
		if(todoItems.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(todoItems);
		}
	}

	@Override
	@PutMapping(value = "/todo",
			consumes = "application/json")
	public ResponseEntity<TodoItem> add(@RequestBody TodoItem todoItem) throws URISyntaxException {
		return ResponseEntity.created(new URI("/todos/" + todoItem.getId()))
				.body(todoService.add(todoItem));
	}

	@Override
	@DeleteMapping(value = "/todo/{id}")
	public ResponseEntity<Void> remove(@PathVariable Long id) {
		todoService.remove(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PostMapping(value = "/todo",
			consumes = "application/json")
	public ResponseEntity<TodoItem> update(@RequestBody TodoItem todoItem) {
		if(todoService.find(todoItem.getId()).getId() == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(todoService.update(todoItem));
	}

}
