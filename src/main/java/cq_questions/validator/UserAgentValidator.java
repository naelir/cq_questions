package cq_questions.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAgentValidator implements IValidator<String> {
	private static final Logger LOG = LoggerFactory.getLogger(UserAgentValidator.class);

	private final String requiredUserAgent;

	public UserAgentValidator(String requiredUserAgent) {
		this.requiredUserAgent = requiredUserAgent;
	}

	@Override
	public boolean isValid(String userAgent) {
		final boolean isValid = this.requiredUserAgent.equals(userAgent);
		LOG.debug("user agent isValid: {}", isValid);
		return isValid;
	}
}
