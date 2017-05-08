package exceptions;

/**
 * @author Simon Olofsson
 */
public class NoEmailFoundException extends Exception {

    public NoEmailFoundException(String message) {
        super(message);
    }
}
