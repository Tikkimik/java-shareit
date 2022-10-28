package ru.practicum.shareit.exceptions;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

class ErrorHandlerTest {

    protected ErrorHandler errorHandler;

    @BeforeEach
    void beforeEach() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleNotFoundParameterException() {
        ErrorHandler errorHandler = new ErrorHandler();
        NotFoundParameterException exception = new NotFoundParameterException("bad");
        errorHandler.handleNotFoundParameterException(exception);
        MatcherAssert.assertThat("NotFoundParameterException \"" + exception.getMessage() + "\".",
                equalTo(errorHandler.handleNotFoundParameterException(exception).getError()));
    }

    @Test
    void handleThrowableException() {
        ErrorHandler errorHandler = new ErrorHandler();
        Throwable exception = new Throwable("Throwable");
        errorHandler.handleThrowableException(exception);
        MatcherAssert.assertThat(exception.getMessage(),
                equalTo(errorHandler.handleThrowableException(exception).getError()));
    }

    @Test
    void handleIncorrectStatusException() {
        ErrorHandler errorHandler = new ErrorHandler();
        IncorrectStatusException exception = new IncorrectStatusException("IncorrectStatusException");
        errorHandler.handleIncorrectStatusException(exception);
        MatcherAssert.assertThat(exception.getMessage(),
                equalTo(errorHandler.handleIncorrectStatusException(exception).getError()));
    }

    @Test
    void handleCreatingException() {
        ErrorHandler errorHandler = new ErrorHandler();
        CreatingException exception = new CreatingException("CreatingException");
        errorHandler.handleCreatingException(exception);
        MatcherAssert.assertThat(exception.getParameter(),
                equalTo(errorHandler.handleCreatingException(exception).getError()));
    }

    @Test
    void handleIncorrectParameterException() {
        ErrorHandler errorHandler = new ErrorHandler();
        IncorrectParameterException exception = new IncorrectParameterException("bad");
        errorHandler.handleIncorrectParameterException(exception);
        MatcherAssert.assertThat("IncorrectParameterException \"" + exception.getParameter() + "\".",
                equalTo(errorHandler.handleIncorrectParameterException(exception).getError()));
    }
}