package cq_questions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@formatter:off
@RestController
@RequestMapping("/api")
public class Controller {
	private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
 
	private static final Integer MAX_QUESTIONS = 2000;

	private static final String TIME_MD5_HASH_HEADER = "time_md5_hash";

	private static final String USER_NAME_MD5_HASH_HEADER = "user_name_md5_hash";

	private final IQuestionFactory questionFactory;

	private final Set<String> alllowedIps;

	private final Set<String> bannedIps;

	private final List<RawSelectable> rawQuestions;

	private final List<RawTip> rawTips;
 

	@Autowired
	public Controller(final Builder builder) {
		this.questionFactory = builder.questionFactory;
		this.alllowedIps = builder.alllowed;
		this.bannedIps = builder.banned;
		this.rawQuestions = builder.questions;
		this.rawTips = builder.tips;
	}

	
	@RequestMapping(produces = "application/json", method = RequestMethod.POST, value = "questions/add")
	public boolean addQuestions(@RequestBody final List<RawSelectable> questions,
			@RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent) {
		if (new UserAgentValidator(userAgent).validate()) { 
			LOG.debug("received {} questions", questions.size());
			return this.rawQuestions.addAll(questions);
		} else
			return false;
	}

	@RequestMapping(produces = "application/json", method = RequestMethod.POST, value = "tips/add")
	public boolean addTips(@RequestBody final List<RawTip> tips,
			@RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent) {
		if (new UserAgentValidator(userAgent).validate()) { 
			LOG.debug("received {} tips", tips.size());
			return this.rawTips.addAll(tips);
		} else
			return false;
	}

	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "allowed-ips")
	public Set<String> allowedIp() {
		return this.alllowedIps;
	}

	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "allow-ip")
	public boolean allowIp(@RequestParam(name = "ip") final String ip) {
		return this.alllowedIps.add(ip);
	}

	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "banned-ips")
	public Set<String> bannedIps() {
		return this.bannedIps;
	}

	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "ban-ip")
	public boolean denyIp(@RequestParam(name = "ip") final String ip) {
		return this.bannedIps.add(ip);
	}

	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public void getFile(
	    final HttpServletResponse response) {
	    try (final InputStream inputStream = Files.newInputStream(Paths.get("cq_questions.log"))) { 
 	      IOUtils.copy(inputStream, response.getOutputStream());
	      response.flushBuffer();
	    } catch (final IOException ex) {
	      LOG.error("Error writing file to output strea ", ex);
 	    }

	}
	
	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "questions")
	public Set<RawSelectable> getQuestions(
			@RequestHeader(value = USER_NAME_MD5_HASH_HEADER, defaultValue = "") final String userNameHash,
			@RequestHeader(value = TIME_MD5_HASH_HEADER, defaultValue = "") final String timeHash,
			@RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent, 
			final HttpServletRequest request) {
		LOG.info("request from ip: {}", request.getRemoteAddr());
		final boolean isRequestAcceptable = 
				new UserHashValidator(userNameHash).validate() && 
				new TimeHashValidator(timeHash).validate() && 
				new UserAgentValidator(userAgent).validate();
		LOG.debug("request is acceptable: {}", isRequestAcceptable);

		if (isRequestAcceptable) { 
			final Set<RawSelectable> questions = new  HashSet<>();
			for (int i = 0; i < MAX_QUESTIONS; i++)
				questions.add(this.questionFactory.getSelectable());
			return questions;
		} else
			return new HashSet<>(); 
	}
	
	
	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "tips")
	public Set<RawTip> getTips(
			@RequestHeader(value = USER_NAME_MD5_HASH_HEADER, defaultValue = "") final String userNameHash,
			@RequestHeader(value = TIME_MD5_HASH_HEADER, defaultValue = "") final String timeHash,
			@RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent,
			final HttpServletRequest request) {
		LOG.info("request from ip: {}", request.getRemoteAddr());

		final boolean isRequestAcceptable = 
				new UserHashValidator(userNameHash).validate() && 
				new TimeHashValidator(timeHash).validate() && 
				new UserAgentValidator(userAgent).validate();
		LOG.debug("request is acceptable: {}", isRequestAcceptable); 

		if (isRequestAcceptable) {
			final Set<RawTip> tips = new  HashSet<>();
			for (int i = 0; i < MAX_QUESTIONS; i++)
				tips.add(this.questionFactory.getTip());
			return tips;
		} else
			return new HashSet<>();
	}
	
	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "questions/count")
	public Integer questionsCount() {
		return this.rawQuestions.size();
	}
	
	@RequestMapping(produces = "application/json", method = RequestMethod.GET, value = "tips/count")
	public Integer tipsCount() {
		return this.rawTips.size();
	}
	
	
	public static final class Builder {
		public IQuestionFactory questionFactory;

		private Set<String> banned;

		private Set<String> alllowed;

		private List<RawSelectable> questions;

		private List<RawTip> tips;

		private IRequestValidator validator;

		public Builder setAlllowed(final Set<String> alllowed) {
			this.alllowed = alllowed;
			return this;
		}

		public Builder setBanned(final Set<String> banned) {
			this.banned = banned;
			return this;
		}

		public Builder setQuestionFactory(final IQuestionFactory questionFactory) {
			this.questionFactory = questionFactory;
			return this;
		}

		public Builder setQuestions(final List<RawSelectable> list) {
			this.questions = list;
			return this;
		}

		public Builder setTips(final List<RawTip> tips) {
			this.tips = tips;
			return this;
		}

		public Builder setValidator(final IRequestValidator validator) {
			this.validator = validator;
			return this;
		}
	}
}
//@formatter:on