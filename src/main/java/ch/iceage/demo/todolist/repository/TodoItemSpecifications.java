package ch.iceage.demo.todolist.repository;

import ch.iceage.demo.todolist.domain.dto.TaskSearchDTO;
import ch.iceage.demo.todolist.domain.Status;
import ch.iceage.demo.todolist.domain.TodoItem;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TodoItemSpecifications {

    public static Specification<TodoItem> hasStatus(Status status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<TodoItem> hasStatusIn(Set<Status> statuses) {
        return (root, query, cb) -> root.get("status").in(statuses);
    }



    public static Specification<TodoItem> filterBy(TaskSearchDTO dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Status filter
            if (dto.statuses() != null && !dto.statuses().isEmpty()) {
                predicates.add(root.get("status").in(dto.statuses()));
            }

            // Title filter
            if (dto.title() != null && dto.title().text() != null) {
                String titleText = dto.title().text();
                if (dto.title().exactMatch()) {
                    predicates.add(cb.equal(cb.lower(root.get("title")), titleText.toLowerCase()));
                } else {
                    predicates.add(cb.like(cb.lower(root.get("title")), "%" + titleText.toLowerCase() + "%"));
                }
            }

            // Date range filter
            if (dto.fromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), dto.fromDate()));
            }
            if (dto.toDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), dto.toDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
