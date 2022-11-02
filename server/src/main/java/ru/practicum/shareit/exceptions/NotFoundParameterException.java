package ru.practicum.shareit.exceptions;

public class NotFoundParameterException extends RuntimeException {

    private final String parameter;

    public NotFoundParameterException(String parameter) {
        super(parameter);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

}
