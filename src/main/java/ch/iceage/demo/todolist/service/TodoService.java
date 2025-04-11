package ch.iceage.demo.todolist.service;

import ch.iceage.demo.todolist.domain.TodoItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TodoService {

	@Transactional
	TodoItem update(TodoItem todoItem);

	/**
	 * Get all items
	 * @return
	 */
	List<TodoItem> getAll();
	
	/**
	 * Get all undone items
	 * @return
	 */
	List<TodoItem> getNotDone();

	/**
	 * Get all done items.
	 *
	 * @return
	 */
	List<TodoItem> getDone();

	TodoItem add(TodoItem todoItem);

	void remove(Long id);

	TodoItem find(Long id);
}