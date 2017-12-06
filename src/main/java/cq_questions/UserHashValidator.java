package cq_questions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserHashValidator implements IRequestValidator {
	private static final Logger LOG = LoggerFactory.getLogger(UserHashValidator.class);

	private static final String DEFAULT_USER_MD5 = "b43a6b9024591ba199aca71c587444a3";

	private final String userHash;

	public UserHashValidator(final String userHash) {
		this.userHash = userHash;
	}

	@Override
	public boolean validate() {
		final boolean isUserHashMatch = DEFAULT_USER_MD5.equals(this.userHash);
		LOG.debug("user hash match: {}", isUserHashMatch);
		return isUserHashMatch;
	}
}
