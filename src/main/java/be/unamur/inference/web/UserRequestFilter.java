package be.unamur.inference.web;

/**
 * Defines methods to filter the {@link UserRequest} of type T.
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The type of {@link UserRequest} to consider.
 */
public interface UserRequestFilter<T extends UserRequest>{
	
	/**
	 * Returns true if the given request has to be filtered.
	 * @param request The request to consider.
	 * @return True if the given request has to be filtered.
	 */
	public boolean filter(T request);

}
