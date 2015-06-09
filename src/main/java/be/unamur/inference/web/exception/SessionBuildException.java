package be.unamur.inference.web.exception;

/**
 * This exception represents an error occuring the building of the sessions.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 */
public class SessionBuildException extends Exception {

	private static final long serialVersionUID = -5256467229942330643L;

	/**
	 * Creates a new {@link SessionBuildException}.
	 * 
	 * @param message The error message of the exception.
	 */
	public SessionBuildException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@link SessionBuildException}.
	 * 
	 * @param message The error message of the exception.
	 * @param cause The cause of the exception.
	 */
	public SessionBuildException(String message, Exception cause) {
		super(message, cause);
	}

}
