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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import be.yami.EntryFilter;

/**
 * This class is used to filter {@link UserRequest} based on their client,
 * resource, request type and time values.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The {@link UserRequest} to filter.
 * @see UserRequest
 */
public class RegExpUserRequestFilter<T extends UserRequest> implements
        EntryFilter<T> {

    private Pattern clientExpr;
    private Pattern resourceExpr;
    private Pattern requestTypeExpr;
    private Date before;
    private Date after;

    /**
     * Creates a new filter
     */
    public RegExpUserRequestFilter() {
    }

    /**
     * Sets the regular expression used to match the client field of the
     * request.
     *
     * @param regExp The regular expression applied to the client field.
     * @return This object.
     * @throws PatternSyntaxException If the regular expression can not be
     * compiled.
     * @see Pattern
     */
    public RegExpUserRequestFilter<T> clientMatches(String regExp)
            throws PatternSyntaxException {
        this.clientExpr = Pattern.compile(regExp);
        return this;
    }

    /**
     * Sets the regular expression used to match the resource field of the
     * request.
     *
     * @param regExp The regular expression applied to the request field.
     * @return This object.
     * @throws PatternSyntaxException If the regular expression can not be
     * compiled.
     * @see Pattern
     */
    public RegExpUserRequestFilter<T> resourceMatches(String regExp)
            throws PatternSyntaxException {
        this.resourceExpr = Pattern.compile(regExp);
        return this;
    }

    /**
     * Sets the regular expression used to match the request type field of the
     * request.
     *
     * @param regExp The regular expression applied to the request type field.
     * @return This object.
     * @throws PatternSyntaxException If the regular expression can not be
     * compiled.
     * @see Pattern
     */
    public RegExpUserRequestFilter<T> requestTypeMatches(String regExp)
            throws PatternSyntaxException {
        this.requestTypeExpr = Pattern.compile(regExp);
        return this;
    }

    /**
     * Sets the minimal date to consider for the time field of the request.
     *
     * @param time The minimal date.
     * @return This object.
     */
    public RegExpUserRequestFilter<T> after(Date time) {
        this.after = time;
        return this;
    }

    /**
     * Sets the maximal date to consider for the time field of the request.
     *
     * @param time The maximal date.
     * @return This object.
     */
    public RegExpUserRequestFilter<T> before(Date time) {
        this.before = time;
        return this;
    }

    @Override
    public boolean filter(T request) {
        boolean ok = true;
        if (ok && this.before != null) {
            ok = ok && request.getTime().before(this.before);
        }
        if (ok && this.after != null) {
            ok = request.getTime().after(this.before);
        }
        if (ok && this.clientExpr != null) {
            ok = this.clientExpr.matcher(request.getClient()).matches();
        }
        if (ok && this.resourceExpr != null) {
            ok = this.resourceExpr.matcher(request.getResource()).matches();
        }
        if (ok && this.requestTypeExpr != null) {
            ok = this.requestTypeExpr.matcher(request.getRequestType()).matches();
        }
        return ok;
    }

}
