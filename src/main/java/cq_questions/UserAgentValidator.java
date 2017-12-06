package cq_questions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAgentValidator implements IRequestValidator {
	private static final Logger LOG = LoggerFactory.getLogger(UserAgentValidator.class);

	private static final String MAIN_SERVER_USER_AGENT = "unirest-java/1.3.11";

	private final String userAgent;

	public UserAgentValidator(final String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public boolean validate() {
		final boolean isUserAgentAcceptable = MAIN_SERVER_USER_AGENT.equals(this.userAgent);
		LOG.debug("user agent match: {}", isUserAgentAcceptable);
		return isUserAgentAcceptable;
	}
}
