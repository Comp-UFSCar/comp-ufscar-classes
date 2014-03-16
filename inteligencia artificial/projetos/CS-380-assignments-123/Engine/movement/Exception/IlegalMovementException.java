package Engine.movement.Exception;

/**
 *
 * @author ld492
 */
public class IlegalMovementException extends RuntimeException {

    public IlegalMovementException() {
        super();
    }

    public IlegalMovementException(String message) {
        super(message);
    }

    public IlegalMovementException(Throwable cause) {
        super(cause);
    }
}
