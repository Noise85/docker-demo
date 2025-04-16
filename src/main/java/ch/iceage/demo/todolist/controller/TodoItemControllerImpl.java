package ch.iceage.demo.todolist.controller;

import ch.iceage.demo.todolist.domain.TodoItem;
import ch.iceage.demo.todolist.domain.dto.TaskSearchDTO;
import ch.iceage.demo.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 11:57:28
 */
@RestController
public class TodoItemControllerImpl implements TodoItemController {

	@Autowired
	private TodoService todoService;

	@Override
	public ResponseEntity<Iterable<TodoItem>> getAllItems(@PageableDefault Pageable page) {
		Iterable<TodoItem> todoItems = todoService.getAll(page);
		if(!todoItems.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.ok(todoItems);
		}
	}

	@Override
	public ResponseEntity<Iterable<TodoItem>> getAllBy(@RequestBody TaskSearchDTO taskSearchDTO) {
		Iterable<TodoItem> todoItems = todoService.filterBy(taskSearchDTO, taskSearchDTO.page().toPageRequest());
		if(!todoItems.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.ok(todoItems);
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
	public ResponseEntity<Iterable<TodoItem>> getByStatus(@PathVariable String status, @PageableDefault Pageable page) {
		Iterable<TodoItem> todoItems = todoService.getByStatus(status, page);
		if(!todoItems.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.ok(todoItems);
		}
	}

	@Override
	public ResponseEntity<Iterable<TodoItem>> getBoardItems(@PageableDefault Pageable page) {
		Iterable<TodoItem> todoItems = todoService.getBoard(page);
		if(!todoItems.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		else {
			return ResponseEntity.ok(todoItems);
		}
	}

	@Override
	public ResponseEntity<Iterable<TodoItem>> getBacklogItems(@PageableDefault Pageable page) {
		Iterable<TodoItem> todoItems = todoService.getBacklog(page);
		if(!todoItems.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		else {
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
