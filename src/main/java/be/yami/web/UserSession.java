package be.yami.web;

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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Represents a sequence of {@link UserRequest}s. Each request follow the
 * previous one in time.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The type of user requests contained in the session.
 */
public class UserSession<T extends UserRequest> implements Iterable<T> {

    /**
     * The id of the user who issued this request.
     */
    private String userId;

    /**
     * The start time of the sequence. Corresponds to the time of the first
     * request of the sequence.
     */
    protected Date startTime;

    /**
     * The end time of the sequence. Corresponds to the time of the last request
     * of the sequence.
     */
    protected Date endTime;

    /**
     * The list of requests.
     */
    private final List<T> session;

    /**
     * Creates a new session for the user identified by the given id.
     *
     * @param userId The id of the user issuing the requests of the session.
     */
    public UserSession(String userId) {
        this.userId = userId;
        this.session = Lists.newArrayList();
    }

    /**
     * Returns the id of the user who issued this request.
     *
     * @return The id of the user who issued this request.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set the id of the user who issued this request.
     *
     * @param userId The new user's id.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the start time of the sequence. Corresponds to the time of the
     * first request of the sequence.
     *
     * @return The start time of the sequence.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the sequence. Corresponds to the time of the last
     * request of the sequence.
     *
     * @return The end time of the sequence.
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Returns the number of requests in the session.
     *
     * @return The number of requests in the session.
     */
    public int size() {
        return session.size();
    }

    @Override
    public String toString() {
        return "UserSession [userId=" + userId + ", startTime=" + startTime
                + ", endTime=" + endTime + ", session=" + session + "]";
    }

    @Override
    public Iterator<T> iterator() {
        return session.iterator();
    }

    /**
     * Add a new request to the session. Requests have to be added using
     * chronological order in the session.
     *
     * @param request The request to add. Has to be later than the last request
     * added to the session (request.getTime() &gt; this.getEndTime() ).
     */
    public void enqueue(T request) {
//		checkArgument((this.endTime == null)
//				|| (request.getTime().compareTo(endTime) >= 0));
        if (this.session.isEmpty()) {
            this.startTime = request.getTime();
        }
        this.session.add(request);
        this.endTime = request.getTime();
    }

}
