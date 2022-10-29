package main.enums;

public enum TaskFromString {
    ID(0),
    TYPE(1),
    TITLE(2),
    STATUS(3),
    DESCRIPTION(4),
    DURATION(5),
    START_DATE(6),
    PARENT_EPIC_ID(7);

    private final int index;

    TaskFromString(final int newValue) {
        index = newValue;
    }

    public int getIndex() {
        return index;
    }
}
