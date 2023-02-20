package io.github.ppetrbednar.dstr.logic.railway.exceptions;

public class NoPathFoundException extends Exception {
    public NoPathFoundException() {
    }

    public NoPathFoundException(String message) {
        super(message);
    }

    public NoPathFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPathFoundException(Throwable cause) {
        super(cause);
    }
}
