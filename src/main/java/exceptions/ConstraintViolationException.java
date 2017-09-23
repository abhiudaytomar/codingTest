package exceptions;

public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
