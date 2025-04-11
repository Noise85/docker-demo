package ch.iceage.demo.todolist.repository;

import ch.iceage.demo.todolist.domain.TodoItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 11:56:56
 */
@Repository
public interface TodoItemRepository extends CrudRepository<TodoItem, Long>, TodoItemCustomRepository {


}
