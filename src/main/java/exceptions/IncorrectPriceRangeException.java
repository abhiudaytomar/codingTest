package exceptions;

public class IncorrectPriceRangeException extends RuntimeException {
    public IncorrectPriceRangeException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
