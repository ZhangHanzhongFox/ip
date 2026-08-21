/**
 * Represents an error caused by an invalid Fox command.
 */
public class FoxException extends Exception {

    /**
     * Creates an exception with a message suitable for display to the user.
     *
     * @param message the explanation of the command error
     */
    public FoxException(String message) {
        super(message);
    }
}
