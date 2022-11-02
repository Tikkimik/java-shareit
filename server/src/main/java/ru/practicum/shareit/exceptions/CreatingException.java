package ru.practicum.shareit.exceptions;

public class CreatingException extends Exception {

    private final String parameter;

    public CreatingException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

}
