package ru.practicum.shareit.exceptions;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

class ErrorHandlerTest {

    ErrorHandler errorHandler;

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
        Throwable exception = new Throwable("bad");
        errorHandler.handleThrowableException(exception);
        MatcherAssert.assertThat(exception.getMessage(),
                equalTo(errorHandler.handleThrowableException(exception).getError()));
    }
}