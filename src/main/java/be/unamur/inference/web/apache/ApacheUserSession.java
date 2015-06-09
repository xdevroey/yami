package be.unamur.inference.web.apache;

import be.unamur.inference.web.UserSession;

/**
 * This class represents sessions on an Apache web server. Those sessions
 * contains {@link ApacheUserRequest}s.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @see ApacheUserRequest
 * 
 */
public class ApacheUserSession extends UserSession<ApacheUserRequest> {

	/**
	 * Creates a new Apache user sessions with the given user id.
	 * 
	 * @param userId The id of the user issuing the session.
	 */
	public ApacheUserSession(String userId) {
		super(userId);
	}

}
