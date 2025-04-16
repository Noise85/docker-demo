package ch.iceage.demo.todolist.domain;

import java.util.EnumSet;

public enum Status {
    BACKLOG,
    READY,
    IN_PROGRESS,
    REVIEW,
    REOPENED,
    DONE;

    public static EnumSet<Status> boardStatuses() {
        return EnumSet.of(READY, IN_PROGRESS, REVIEW, REOPENED, DONE);
    }
}
