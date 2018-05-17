package be.yami;

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

import be.yami.exception.SessionBuildException;

/**
 * Represents an abstract SequenceBuilder. This object encapsulates methods to
 * manage the building, filtering and dispatching the sequences get from an
 * InputStream. The filtering is done using include and exclude EntryFilters and
 * the processing is delegated to registered listeners.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The type of sequence to consider.
 * @param <U> The type of entries in the sequence to consider.
 */
public abstract class SequenceBuilder<T extends Iterable<U>, U> {

    /**
     * Listener notified when a session is built.
     */
    private Vector<SequenceProcessor<T>> listeners;

    /**
     * Only entries satisfying all the include filters are considered for
     * sessions.
     */
    private Vector<EntryFilter<U>> includes;

    /**
     * Only entries that does not satisfy all the exclude filters are
     * considered for sessions.
     */
    private Vector<EntryFilter<U>> excludes;

    /**
     * Creates a new user session builder.
     */
    public SequenceBuilder() {
        this.listeners = new Vector<>();
        this.includes = new Vector<>();
        this.excludes = new Vector<>();
    }

    /**
     * Add a listener notified when a session is built (during buildSession
     * call).
     *
     * @param listener The listener to add.
     * @return This object.
     */
    public SequenceBuilder<T, U> addListener(SequenceProcessor<T> listener) {
        this.listeners.add(listener);
        return this;
    }

    /**
     * Notifies the listeners that the given session has been built.
     *
     * @param session The session given to the listeners.
     */
    protected void sessionCompleted(T session) {
        for (SequenceProcessor<T> p : this.listeners) {
            p.process(session);
        }
    }

    /**
     * Add an include filter to this builder. Only entries satisfying all the
     * include filters are added to sessions.
     *
     * @param filter The filter to add
     * @return This object.
     */
    public SequenceBuilder<T, U> include(EntryFilter<U> filter) {
        this.includes.add(filter);
        return this;
    }

    /**
     * Add an include filter to this builder. Only entries that does not
     * satisfy all the exclude filters are added to sessions.
     *
     * @param filter The filter to add
     * @return This object.
     */
    public SequenceBuilder<T, U> exclude(EntryFilter<U> filter) {
        this.excludes.add(filter);
        return this;
    }

    /**
     * Check if the entry is accepted according to the include and exclude
     * filters.
     *
     * @param req The entry to process.
     * @return True if the entry satisfy all the include filters and does not
     * satisfy all the exclude filters.
     */
    protected boolean isAcceptedRequest(U req) {
        boolean ok = true;
        Iterator<EntryFilter<U>> it = this.includes.iterator();
        EntryFilter<U> f;
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
     * Build the session from the different entry of the {@link InputStream}.
     * Each time a session is build, the sessionCompleted(Session) method is
     * called to notify the listeners of the builder. All the entries in the
     * builded sessions have to satisfy isAcceptedRequest(Request).
     *
     * @param input The {@link InputStream} to read.
     * @throws SessionBuildException If an exception occurs during the building
     * of the sessions.
     */
    public abstract void buildSessions(InputStream input) throws SessionBuildException;

}
