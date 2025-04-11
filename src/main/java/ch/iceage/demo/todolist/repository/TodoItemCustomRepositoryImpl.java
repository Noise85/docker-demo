package ch.iceage.demo.todolist.repository;

import ch.iceage.demo.todolist.domain.Status;
import ch.iceage.demo.todolist.domain.TodoItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

public class TodoItemCustomRepositoryImpl implements TodoItemCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TodoItem> findByStatus(Status status) {
        return entityManager.createQuery("SELECT t FROM TodoItem t WHERE t.status = :status", TodoItem.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<TodoItem> findByStatusIn(Set<Status> status) {
        return entityManager.createQuery("SELECT t FROM TodoItem t WHERE t.status IN :status", TodoItem.class)
                .setParameter("status", status)
                .getResultList();
    }

}
