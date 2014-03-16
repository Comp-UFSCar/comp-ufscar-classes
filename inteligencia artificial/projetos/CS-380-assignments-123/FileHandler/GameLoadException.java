package FileHandler;

/**
 *
 * @author ld492
 */
public class GameLoadException extends RuntimeException {

    public GameLoadException() {
        super();
    }

    public GameLoadException(String message) {
        super(message);
    }

    public GameLoadException(Throwable cause) {
        super(cause);
    }
}
