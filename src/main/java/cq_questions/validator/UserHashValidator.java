package cq_questions.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserHashValidator implements IValidator<String> {
	private static final Logger LOG = LoggerFactory.getLogger(UserHashValidator.class);

	private final String requiredMd5;

	public UserHashValidator(final String requiredMd5) {
		this.requiredMd5 = requiredMd5;
	}

	@Override
	public boolean isValid(String md5) {
		final boolean isValid = this.requiredMd5.equals(md5);
		LOG.debug("user hash is valid: {}", isValid);
		return isValid;
	}
}
