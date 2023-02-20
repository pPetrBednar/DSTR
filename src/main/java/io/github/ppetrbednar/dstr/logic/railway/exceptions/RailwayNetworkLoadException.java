package io.github.ppetrbednar.dstr.logic.railway.exceptions;

public class RailwayNetworkLoadException extends Exception {
    public RailwayNetworkLoadException() {
    }

    public RailwayNetworkLoadException(String message) {
        super(message);
    }

    public RailwayNetworkLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RailwayNetworkLoadException(Throwable cause) {
        super(cause);
    }
}
