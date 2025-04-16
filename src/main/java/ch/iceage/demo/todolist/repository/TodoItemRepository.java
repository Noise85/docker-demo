package ch.iceage.demo.todolist.repository;

import ch.iceage.demo.todolist.domain.TodoItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Enea Bettè
 * @since Aug 03, 2017 11:56:56
 */
@Repository
public interface TodoItemRepository extends PagingAndSortingRepository<TodoItem, Long>, JpaSpecificationExecutor<TodoItem> {

}
