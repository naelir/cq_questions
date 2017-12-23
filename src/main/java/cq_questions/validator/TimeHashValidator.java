package cq_questions.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

public class TimeHashValidator implements IValidator<String> {
	private static final Logger LOG = LoggerFactory.getLogger(TimeHashValidator.class);

	private final int validTime;

	public TimeHashValidator(final int validTime) {
		this.validTime = validTime;
	}

	@Override
	public boolean isValid(String hash) {
		boolean isValid = false;
		final long seconds = System.currentTimeMillis() / 1000;
		for (int i = 0; i < this.validTime; i++) {
			final long current = seconds - i;
			final String md5 = DigestUtils.md5DigestAsHex(String.valueOf(current).getBytes());
			if (hash.equals(md5)) {
				isValid = true;
				break;
			}
		}
		LOG.info("time hash is valid: {}", isValid);
		return isValid;
	}
}
