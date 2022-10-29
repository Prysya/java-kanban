package main.utils;

import main.task.Task;

import java.util.Comparator;
import java.util.Objects;

public class CustomComparators {
    private CustomComparators() {
    }

    public static Comparator<Task> byStartTime() {
        return (o1, o2) -> {
            if (Objects.isNull(o1.getStartTime()) && Objects.isNull(o2.getStartTime())) {
                return o1.getId() - o2.getId();
            }

            if (Objects.isNull(o1.getStartTime())) {
                return 1;
            }

            if (Objects.isNull(o2.getStartTime())) {
                return -1;
            }

            return o1.getStartTime().compareTo(o2.getStartTime());
        };
    }
}
