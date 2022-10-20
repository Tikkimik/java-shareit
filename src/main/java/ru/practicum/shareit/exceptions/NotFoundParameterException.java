package ru.practicum.shareit.exceptions;

public class NotFoundParameterException extends Exception {

    private final String parameter;

    public NotFoundParameterException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

}
