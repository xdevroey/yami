package be.unamur.inference.web.apache;

import java.util.Arrays;

import be.unamur.inference.ngram.ObjectKeyGenerator;

/**
 * Request Resource Parameter Names key generator. Generates a key equal to the
 * type of request (HEAD, GET, POST), the resource name and the names of the
 * parameters. Parameters are sorted by name in the key.
 * 
 * @author Xavier Devroey <xavier.devroey@unamur.be>
 * 
 */
public class UserRequesRRNKeyGenerator implements ObjectKeyGenerator<ApacheUserRequest> {

	private static UserRequesRRNKeyGenerator instance = null;

	private UserRequesRRNKeyGenerator() {
	}

	/**
	 * Returns the singleton instance of this class.
	 */
	public static UserRequesRRNKeyGenerator getInstance() {
		return instance == null ? instance = new UserRequesRRNKeyGenerator() : instance;
	}

	@Override
	public String generateKey(ApacheUserRequest request) {
		StringBuffer buff = new StringBuffer(String.format("%s %s?",
				request.getRequestType(), request.getResource()));
		String[] parameters = Arrays.copyOf(request.getParameters(),
				request.getParameters().length);
		Arrays.sort(parameters);
		for (int i = 0; i < parameters.length; i++) {
			if (i > 0) {
				buff.append("&");
			}
			buff.append(parameters[i]);
			buff.append("=");
		}
		return buff.toString();
	}

}
