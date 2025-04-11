package ch.iceage.demo.todolist.service;

import ch.iceage.demo.todolist.domain.Status;
import ch.iceage.demo.todolist.domain.TodoItem;
import ch.iceage.demo.todolist.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 12:10:06
 */
@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoItemRepository todoItemRepository;

	@Override
	public List<TodoItem> getNotDone() {
		return todoItemRepository.findByStatusIn(
                EnumSet.allOf(Status.class)
                        .stream()
                        .filter(status -> !status.equals(Status.DONE))
                        .collect(Collectors.toSet()));
	}
	
	@Override
	public List<TodoItem> getDone() {
		return todoItemRepository.findByStatus(Status.DONE);
	}

	@Override
	@Transactional
	public TodoItem add(TodoItem todoItem) {
		return todoItemRepository.save(todoItem);
	}

	@Override
	@Transactional
	public void remove(Long id) {
		todoItemRepository.deleteById(id);
	}

	@Override
	public TodoItem find(Long id) {
		return todoItemRepository.findById(id).orElse(new TodoItem());
	}

	@Override
	@Transactional
	public TodoItem update(TodoItem todoItem) {
		if(todoItem.getId()!=null) {
			return todoItemRepository.save(todoItem);
		} else {
			throw new PersistenceException("TodoItem ID cannot be null");
		}
	}

	@Override
	public List<TodoItem> getAll() {
		return StreamSupport.stream(todoItemRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
    }

}
