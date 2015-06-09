package be.unamur.inference.web;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import static com.google.common.base.Preconditions.*;

/**
 * Represents a sequence of {@link UserRequest}s. Each request follow the
 * previous one in time.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 * @param <T>
 */
public class UserSession<T extends UserRequest> implements Iterable<T> {

	/**
	 * The id of the user who issued this request.
	 */
	private String userId;
	
	/**
	 * The start time of the sequence. Corresponds to the time of the first request of the sequence.
	 */
	protected Date startTime;
	
	/**
	 * The end time of the sequence. Corresponds to the time of the last request of the sequence.
	 */
	protected Date endTime;
	
	/**
	 * The list of requests.
	 */
	private List<T> session;

	/**
	 * Creates a new session for the user identified by the given id.
	 * @param userId The id of the user issuing the requests of the session.
	 */
	public UserSession(String userId) {
		this.userId = userId;
		this.session = Lists.newArrayList();
	}

	/**
	 * Returns the id of the user who issued this request.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the id of the user who issued this request.
	 * @param userId The new user's id.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the start time of the sequence. Corresponds to the time of the first request of the sequence.
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Returns the end time of the sequence. Corresponds to the time of the last request of the sequence.
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Returns the number of requests in the session.
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
	 * Add a new request to the session. Requests have to be added using chronological order in the session.
	 * @param request The request to add. Has to be later than the last request
	 *            added to the session (request.getTime() > this.getEndTime() ).
	 */
	public void enqueue(T request) {
		checkArgument((this.endTime == null)
				|| (request.getTime().compareTo(endTime) >= 0));
		if (this.session.isEmpty()) {
			this.startTime = request.getTime();
		}
		this.session.add(request);
		this.endTime = request.getTime();
	}

}
