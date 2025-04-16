package ch.iceage.demo.todolist.service;

import ch.iceage.demo.todolist.domain.TodoItem;
import ch.iceage.demo.todolist.domain.dto.TaskSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TodoService {

	@Transactional
	TodoItem update(TodoItem todoItem);

	Iterable<TodoItem> getAll();

	Iterable<TodoItem> getAll(Pageable page);

	List<TodoItem> filterBy(TaskSearchDTO taskSearchDTO);

	Iterable<TodoItem> filterBy(TaskSearchDTO taskSearchDTO, Pageable page);

	List<TodoItem> getBacklog();

	Iterable<TodoItem> getBacklog(Pageable page);

	List<TodoItem> getBoard();

	Iterable<TodoItem> getBoard(Pageable page);

	TodoItem add(TodoItem todoItem);

	void remove(Long id);

	TodoItem find(Long id);

	Iterable<TodoItem> getByStatus(String status, Pageable page);
}