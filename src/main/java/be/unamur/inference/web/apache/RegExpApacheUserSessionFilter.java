package be.unamur.inference.web.apache;

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
