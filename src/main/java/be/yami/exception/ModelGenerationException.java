package be.yami.exception;

/**
 * Represents an error during the generation of a usage model.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class ModelGenerationException extends Exception {

    /**
     * Creates a new {@link ModelGenerationException}.
     *
     * @param message The error message of the exception.
     */
    public ModelGenerationException(String message) {
        super(message);
    }

    /**
     * Creates a new {@link ModelGenerationException}.
     *
     * @param message The error message of the exception.
     * @param cause The cause of the exception.
     */
    public ModelGenerationException(String message, Exception cause) {
        super(message, cause);
    }

}
