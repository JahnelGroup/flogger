package com.jahnelgroup.flogger;

public class FloggerException extends Exception {

    public FloggerException() {
        super();
    }

    public FloggerException(String message) {
        super(message);
    }

    public FloggerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FloggerException(Throwable cause) {
        super(cause);
    }
}
