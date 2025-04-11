package ch.iceage.demo.todolist.repository;

import ch.iceage.demo.todolist.domain.Status;
import ch.iceage.demo.todolist.domain.TodoItem;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

@NoRepositoryBean
interface TodoItemCustomRepository {
    List<TodoItem> findByStatus(@Param("status") Status status);

    List<TodoItem> findByStatusIn(@Param("status") Set<Status> status);
}
