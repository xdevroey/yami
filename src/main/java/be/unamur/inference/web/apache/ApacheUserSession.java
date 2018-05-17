package be.unamur.inference.web.apache;

/*-
 * #%L
 * YAMI - Yet Another Model Inference tool
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
