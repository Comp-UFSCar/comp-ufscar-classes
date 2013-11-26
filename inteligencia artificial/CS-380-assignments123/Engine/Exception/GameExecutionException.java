package Engine.Exception;

/**
 *
 * @author ld492
 */
public class GameExecutionException extends RuntimeException {

    public GameExecutionException() {
        super();
    }

    public GameExecutionException(String message) {
        super(message);
    }

    public GameExecutionException(Throwable cause) {
        super(cause);
    }
}
