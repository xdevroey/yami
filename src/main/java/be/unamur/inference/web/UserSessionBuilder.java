package be.unamur.inference.web;

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

import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import be.unamur.inference.web.exception.SessionBuildException;

/**
 * Represents an abstract {@link UserSessionBuilder}. This object encapsulates
 * methods to manage the building, filtering and dispatching the
 * {@link UserSession}s get from an {@link InputStream}. The filtering is done
 * using include and exclude {@link UserRequestFilter}s and the processing is
 * delegated to {@link UserSessionProcessor}s registered as listeners of this
 * builder object.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 * @param <T> The type of {@link UserSession} to consider.
 * @param <U> The type of {@link UserRequest} to consider.
 */
public abstract class UserSessionBuilder<T extends UserSession<U>, U extends UserRequest> {

	/**
	 * Listener notified when a session is built.
	 */
	private Vector<UserSessionProcessor<T>> listeners;

	/**
	 * Only requests satisfying all the include filters are considered for
	 * sessions.
	 */
	private Vector<UserRequestFilter<U>> includes;

	/**
	 * Only requests that does not satisfy all the exclude filters are
	 * considered for sessions.
	 */
	private Vector<UserRequestFilter<U>> excludes;

	/**
	 * Creates a new user session builder.
	 */
	public UserSessionBuilder() {
		this.listeners = new Vector<UserSessionProcessor<T>>();
		this.includes = new Vector<UserRequestFilter<U>>();
		this.excludes = new Vector<UserRequestFilter<U>>();
	}

	/**
	 * Add a listener notified when a session is built (during buildSession
	 * call).
	 * 
	 * @param listener The listener to add.
	 * @return This object.
	 */
	public UserSessionBuilder<T, U> addListener(UserSessionProcessor<T> listener) {
		this.listeners.add(listener);
		return this;
	}

	/**
	 * Notifies the listeners that the given session has been built.
	 * 
	 * @param session The session given to the listeners.
	 */
	protected void sessionCompleted(T session) {
		for (UserSessionProcessor<T> p : this.listeners) {
			p.process(session);
		}
	}

	/**
	 * Add an include filter to this builder. Only requests satisfying all the
	 * include filters are added to sessions.
	 * 
	 * @param filter The filter to add
	 * @return This object.
	 */
	public UserSessionBuilder<T, U> include(UserRequestFilter<U> filter) {
		this.includes.add(filter);
		return this;
	}

	/**
	 * Add an include filter to this builder. Only requests that does not
	 * satisfy all the exclude filters are added to sessions.
	 * 
	 * @param filter The filter to add
	 * @return This object.
	 */
	public UserSessionBuilder<T, U> exclude(UserRequestFilter<U> filter) {
		this.excludes.add(filter);
		return this;
	}

	/**
	 * Check if the request is accepted according to the include and exclude
	 * filters.
	 * 
	 * @param req The request to process.
	 * @return True if the request satisfy all the include filters and does not
	 *         satisfy all the exclude filters.
	 */
	protected boolean isAcceptedRequest(U req) {
		boolean ok = true;
		Iterator<UserRequestFilter<U>> it = this.includes.iterator();
		UserRequestFilter<U> f;
		// Check includes
		while (ok && it.hasNext()) {
			f = it.next();
			ok = ok && f.filter(req);
		}
		// Check excludes
		it = this.excludes.iterator();
		while (ok && it.hasNext()) {
			f = it.next();
			ok = ok && !f.filter(req);
		}
		return ok;
	}

	/**
	 * Build the session from the different request of the {@link InputStream}.
	 * Each time a session is build, the sessionCompleted(Session) method is
	 * called to notify the listeners of the builder. All the requests in the
	 * builded sessions have to satisfy isAcceptedRequest(Request).
	 * 
	 * @param input The {@link InputStream} to read.
	 * @throws SessionBuildException If an exception occurs during the building
	 *             of the sessions.
	 */
	public abstract void buildSessions(InputStream input) throws SessionBuildException;

}
