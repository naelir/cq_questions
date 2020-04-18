package cq_questions.http;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cq_questions.factory.IQuestionFactory;
import cq_questions.model.Selectable;
import cq_questions.model.Tipable;
import cq_questions.validator.IValidator;
import cq_questions.validator.UserAgentValidator;
import cq_questions.validator.UserHashValidator;

//@formatter:off
@RestController
@RequestMapping("/api")
public class Controller {
	private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

	private static final Integer MAX_QUESTIONS = 2000;

	private static final String USER_NAME_MD5_HASH_HEADER = "user_name_md5_hash";

	private static final String REQUIRED_USER_AGENT = "unirest-java/1.3.11";

	private static final IValidator<String> USER_AGENT_VALIDATOR = new UserAgentValidator(REQUIRED_USER_AGENT);

	private static final String REQUIRED_USER_MD5 = "b43a6b9024591ba199aca71c587444a3";

	private static final IValidator<String> USER_HASH_VALIDATOR = new UserHashValidator(REQUIRED_USER_MD5);

	private final IQuestionFactory questionFactory;

	@Autowired
	public Controller(final Builder builder) {
		this.questionFactory = builder.questionFactory;
	}


	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "questions")
	public Set<Selectable> getSelectables(
			@RequestHeader(value = USER_NAME_MD5_HASH_HEADER, defaultValue = "") final String userNameHash,
			@RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent) {
		final boolean isRequestValid =
				USER_HASH_VALIDATOR.isValid(userNameHash) &&
				USER_AGENT_VALIDATOR.isValid(userAgent);
		LOG.debug("request is acceptable: {}", isRequestValid);
		if (isRequestValid) {
			final Set<Selectable> questions = new  HashSet<>();
			for (int i = 0; i < MAX_QUESTIONS; i++)
				questions.add(this.questionFactory.getSelectable());
			return questions;
		} else
			return new HashSet<>();
	}


	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "tips")
	public Set<Tipable> getTipables(
			@RequestHeader(value = USER_NAME_MD5_HASH_HEADER, defaultValue = "") final String userNameHash,
			@RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent) {
		final boolean isRequestValid =
				USER_HASH_VALIDATOR.isValid(userNameHash) &&
				USER_AGENT_VALIDATOR.isValid(userAgent);
		LOG.debug("request is acceptable: {}", isRequestValid);
		if (isRequestValid) {
			final Set<Tipable> tips = new HashSet<>();
			for (int i = 0; i < MAX_QUESTIONS; i++)
				tips.add(this.questionFactory.getTip());
			return tips;
		} else
			return new HashSet<>();
	}
	//@formatter:on

	public static final class Builder {
		IQuestionFactory questionFactory;

		Set<String> bannedIps;

		Set<String> allowedIps;

		Map<String, Selectable> selectables;

		Map<String, Tipable> tipables;

		public Builder setAlllowedIps(Set<String> allowedIps) {
			this.allowedIps = allowedIps;
			return this;
		}

		public Builder setBannedIps(Set<String> bannedIps) {
			this.bannedIps = bannedIps;
			return this;
		}

		public Builder setQuestionFactory(final IQuestionFactory questionFactory) {
			this.questionFactory = questionFactory;
			return this;
		}

		public Builder setSelectables(Map<String, Selectable> selectables) {
			this.selectables = selectables;
			return this;
		}

		public Builder setTipables(Map<String, Tipable> tipables) {
			this.tipables = tipables;
			return this;
		}
	}
}
//@formatter:on