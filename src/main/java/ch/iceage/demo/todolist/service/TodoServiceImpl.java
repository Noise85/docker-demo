package ch.iceage.demo.todolist.service;

import ch.iceage.demo.todolist.domain.Status;
import ch.iceage.demo.todolist.domain.TodoItem;
import ch.iceage.demo.todolist.domain.dto.TaskSearchDTO;
import ch.iceage.demo.todolist.repository.TodoItemRepository;
import ch.iceage.demo.todolist.repository.TodoItemSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 12:10:06
 */
@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoItemRepository todoItemRepository;

	@Override
	public TodoItem find(Long id) {
		return todoItemRepository.findById(id).orElse(new TodoItem());
	}

	@Override
	public Iterable<TodoItem> getByStatus(String status, Pageable page) {
		return todoItemRepository.findAll(
				Specification.where(TodoItemSpecifications.hasStatus(Status.valueOf(status))), page);
	}

	@Override
	public Iterable<TodoItem> getAll() {
		return todoItemRepository.findAll();
	}

	@Override
	public Iterable<TodoItem> getAll(Pageable page) {
		if(page == null) {
			return todoItemRepository.findAll();
		}
		else {
			return todoItemRepository.findAll(page);
		}
	}

	@Override
	public List<TodoItem> filterBy(TaskSearchDTO taskSearchDTO) {
		return todoItemRepository.findAll(
				Specification.where(TodoItemSpecifications.filterBy(taskSearchDTO)));
	}

	@Override
	public Page<TodoItem> filterBy(TaskSearchDTO taskSearchDTO, Pageable page) {
		return todoItemRepository.findAll(
				Specification.where(TodoItemSpecifications.filterBy(taskSearchDTO)), page);
	}

	@Override
	public Iterable<TodoItem> getBacklog(Pageable page) {
		if(page == null) {
			return todoItemRepository.findAll(Specification.where(
					TodoItemSpecifications.hasStatus(Status.BACKLOG)));
		}
		else {
			return todoItemRepository.findAll(Specification.where(
					TodoItemSpecifications.hasStatus(Status.BACKLOG)), page);
		}
	}

	@Override
	public List<TodoItem> getBacklog() {
		return todoItemRepository
				.findAll(Specification.where(TodoItemSpecifications.hasStatus(Status.BACKLOG)));
	}
	
	@Override
	public List<TodoItem> getBoard() {
		return todoItemRepository.findAll(Specification.where(
				TodoItemSpecifications.hasStatusIn(Status.boardStatuses())));
	}

	@Override
	public Iterable<TodoItem> getBoard(Pageable page) {
		if(page == null) {
			return todoItemRepository.findAll(Specification.where(
					TodoItemSpecifications.hasStatusIn(Status.boardStatuses())));
		}
		else {
			return todoItemRepository.findAll(Specification.where(
					TodoItemSpecifications.hasStatusIn(Status.boardStatuses())), page);
		}
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
	@Transactional
	public TodoItem update(TodoItem todoItem) {
		if(todoItem.getId()!=null) {
			return todoItemRepository.save(todoItem);
		} else {
			throw new PersistenceException("TodoItem ID cannot be null");
		}
	}

}
