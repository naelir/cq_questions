package cq_questions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

public class TimeHashValidator implements IRequestValidator {
	private static final Logger LOG = LoggerFactory.getLogger(TimeHashValidator.class);

	private static final int REQUEST_IS_VALID_LAST_N_SECONDS = 60;

	private final String timeHash;

	public TimeHashValidator(final String timeHash) {
		this.timeHash = timeHash;
	}

	@Override
	public boolean validate() {
		boolean isTimeHashMatch = false;
		final long seconds = System.currentTimeMillis() / 1000;
		for (int i = 0; i < REQUEST_IS_VALID_LAST_N_SECONDS; i++) {
			final long current = seconds - i;
			final String md5 = DigestUtils.md5DigestAsHex(String.valueOf(current).getBytes());
			LOG.debug("current second: {}, current hash: {}", current, md5);
			if (this.timeHash.equals(md5)) {
				isTimeHashMatch = true;
				break;
			}
		}
		LOG.info("time hash match: {}", isTimeHashMatch);
		return isTimeHashMatch;
	}
}
