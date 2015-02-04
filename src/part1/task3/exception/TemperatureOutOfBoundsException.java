package part1.task3.exception;

public class TemperatureOutOfBoundsException extends Exception {
    public TemperatureOutOfBoundsException() {
    }

    public TemperatureOutOfBoundsException(String message) {
        super(message);
    }

    public TemperatureOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemperatureOutOfBoundsException(Throwable cause) {
        super(cause);
    }
}