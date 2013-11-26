/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyzer.Exception;

/**
 *
 * @author Lucas David
 */
public class InvalidGameLoadedException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>InvalidGameLoadedException</code> without detail message.
     */
    public InvalidGameLoadedException() {
    }

    /**
     * Constructs an instance of
     * <code>InvalidGameLoadedException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public InvalidGameLoadedException(String msg) {
        super(msg);
    }
}
