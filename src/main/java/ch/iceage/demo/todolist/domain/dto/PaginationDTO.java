package ch.iceage.demo.todolist.domain.dto;

import org.springframework.data.domain.PageRequest;

import java.util.List;

public record PaginationDTO(int page, int size, List<SortDTO> sort) {

    public PageRequest toPageRequest() {
        return PageRequest.of(page, size, sort.stream()
                .map(sortDTO -> org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.fromString(sortDTO.direction()),
                        sortDTO.property()))
                .reduce(org.springframework.data.domain.Sort.unsorted(), org.springframework.data.domain.Sort::and));
    }
}
