package io.github.ppetrbednar.dstr.logic.railway.exceptions;

public class NoPossiblePathException extends Exception {
    public NoPossiblePathException() {
    }

    public NoPossiblePathException(String message) {
        super(message);
    }

    public NoPossiblePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPossiblePathException(Throwable cause) {
        super(cause);
    }
}
