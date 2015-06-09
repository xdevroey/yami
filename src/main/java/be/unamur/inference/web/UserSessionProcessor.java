package be.unamur.inference.web;

/**
 * Interface declaring methods to process the sessions built by a
 * {@link UserSessionBuilder} object.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 * @param <T> The type of {@link UserSession} built.
 */
public interface UserSessionProcessor<T extends UserSession<? extends UserRequest>> {

	/**
	 * This method is called by the {@link UserSessionBuilder} when a session
	 * has been built.
	 * 
	 * @param session The built session.
	 */
	public void process(T session);

}
