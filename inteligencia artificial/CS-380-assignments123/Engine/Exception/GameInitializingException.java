package Engine.Exception;

/**
 *
 * @author ld492
 */
public class GameInitializingException extends RuntimeException {

    public GameInitializingException() {
        super();
    }

    public GameInitializingException(String message) {
        super(message);
    }

    public GameInitializingException(Throwable cause) {
        super(cause);
    }
}
