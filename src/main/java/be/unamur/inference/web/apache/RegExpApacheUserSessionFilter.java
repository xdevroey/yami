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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import be.unamur.inference.web.RegExpUserRequestFilter;

public class RegExpApacheUserSessionFilter extends RegExpUserRequestFilter<ApacheUserRequest> {
	
	private Pattern refererExpr;
	private Pattern userAgentExpr;
	private int[] statusCodeAccepted;
	
	public RegExpApacheUserSessionFilter() {
		super();
	}
	
	public RegExpApacheUserSessionFilter refererMatches(String regExp) throws PatternSyntaxException {
		this.refererExpr = Pattern.compile(regExp); 
		return this;
	}
	
	public RegExpApacheUserSessionFilter userAgentMatches(String regExp) throws PatternSyntaxException {
		this.userAgentExpr = Pattern.compile(regExp); 
		return this;
	}
	
	public RegExpApacheUserSessionFilter statusCodeIn(int[] acceptedCodes){
		this.statusCodeAccepted = acceptedCodes;
		return this;
	}
	
	@Override
	public boolean filter(ApacheUserRequest request) {
		boolean ok = super.filter(request);
		if(ok && this.refererExpr != null){
			ok = this.refererExpr.matcher(request.getReferrer()).matches();
		}
		if(ok && this.userAgentExpr != null){
			ok = this.userAgentExpr.matcher(request.getUserAgent()).matches();
		}
		if(ok && this.statusCodeAccepted != null){
			ok = contains(this.statusCodeAccepted, request.getStatusCode());
		}
		return ok;
	}

	private boolean contains(int[] tab, int value) {
		boolean found = false;
		int i = 0;
		while(! found && i < tab.length){
			found = tab[i] == value;
			i++;
		}
		return found;
	}
	

}
