package part1.task3.exception;

public class MilliliterOutOfBoundsException extends Exception {

    public MilliliterOutOfBoundsException() {
    }

    public MilliliterOutOfBoundsException(String message) {
        super(message);
    }

    public MilliliterOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MilliliterOutOfBoundsException(Throwable cause) {
        super(cause);
    }

    public MilliliterOutOfBoundsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
