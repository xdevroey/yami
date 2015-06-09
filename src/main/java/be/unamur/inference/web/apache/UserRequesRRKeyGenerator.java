package be.unamur.inference.web.apache;

import be.unamur.inference.ngram.ObjectKeyGenerator;

/**
 * Request Resource key generator. Generates a key equal to the type of request
 * (HEAD, GET, POST) and the resource name.
 * 
 * @author Xavier Devroey <xavier.devroey@unamur.be>
 * 
 */
public class UserRequesRRKeyGenerator implements ObjectKeyGenerator<ApacheUserRequest> {

	private static UserRequesRRKeyGenerator instance = null;

	private UserRequesRRKeyGenerator() {
	}

	/**
	 * Returns the singleton instance of this class.
	 */
	public static UserRequesRRKeyGenerator getInstance() {
		return instance == null ? instance = new UserRequesRRKeyGenerator() : instance;
	}

	@Override
	public String generateKey(ApacheUserRequest request) {
		return String.format("%s %s", request.getRequestType(), request.getResource());
	}

}
