package exceptions;

public class InvalidPriceRangeException extends RuntimeException {
    public InvalidPriceRangeException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
