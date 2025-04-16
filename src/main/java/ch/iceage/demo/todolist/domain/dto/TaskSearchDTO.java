package ch.iceage.demo.todolist.domain.dto;

import ch.iceage.demo.todolist.domain.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record TaskSearchDTO (PaginationDTO page, TitleDTO title, List<Status> statuses,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) { }

