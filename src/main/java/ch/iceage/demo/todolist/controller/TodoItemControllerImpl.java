package ch.iceage.demo.todolist.controller;

import ch.iceage.demo.todolist.domain.TodoItem;
import ch.iceage.demo.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 11:57:28
 */
@RestController
public class TodoItemControllerImpl implements TodoItemController {

	@Autowired
	private TodoService todoService;

	@Override
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
	public ResponseEntity<TodoItem> getById(@PathVariable Long id) {
		TodoItem todoItem = todoService.find(id);
		if(todoItem.getId() == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(todoItem);
		}
	}

	@Override
	public ResponseEntity<List<TodoItem>> getAllDone() {
		List<TodoItem> todoItems = todoService.getDone();
		if(todoItems.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(todoItems);
		}
	}

	@Override
	public ResponseEntity<List<TodoItem>> getAllNotDone() {
		List<TodoItem> todoItems = todoService.getNotDone();
		if(todoItems.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(todoItems);
		}
	}

	@Override
	public ResponseEntity<TodoItem> add(@RequestBody TodoItem todoItem) throws URISyntaxException {
		return ResponseEntity.created(new URI("/todos/" + todoItem.getId()))
				.body(todoService.add(todoItem));
	}

	@Override
	public ResponseEntity<Void> remove(@PathVariable Long id) {
		todoService.remove(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<TodoItem> update(@RequestBody TodoItem todoItem) {
		if(todoService.find(todoItem.getId()).getId() == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(todoService.update(todoItem));
	}

}
