package ru.practicum.shareit.exceptions;

public class IncorrectStatusException extends RuntimeException {

    private final String parameter;

    public IncorrectStatusException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

}