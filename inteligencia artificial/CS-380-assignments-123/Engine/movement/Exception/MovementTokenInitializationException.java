package Engine.movement.Exception;

/**
 *
 * @author ld492
 */
public class MovementTokenInitializationException extends RuntimeException {

    public MovementTokenInitializationException() {
        super();
    }

    public MovementTokenInitializationException(String message) {
        super(message);
    }

    public MovementTokenInitializationException(Throwable cause) {
        super(cause);
    }
}
