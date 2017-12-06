package cq_questions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {
	public static final String DEFAULT_ENCODING = "Cp1251";

	public static final String CURRENT_PASSWORD = "niflheim";

	public static final String QUESTIONS_FILE = "questions-encoded.txt";

	@Bean
	public Controller.Builder main() {
		final Set<String> allowedIps = new HashSet<>();
		final Set<String> bannedIps = new HashSet<>();
		final Questions load = new FileQuestionsLoader(QUESTIONS_FILE, DEFAULT_ENCODING, CURRENT_PASSWORD).load();
		final List<RawSelectable> selectables = load.getRawQuestions();
		final List<RawTip> tips = load.getTips();
		final IQuestionFactory questionFactory = new QuestionFactory(selectables, tips);
		final Controller.Builder builder = new Controller.Builder();
		//@formatter:off
		builder
			.setAlllowed(allowedIps)
			.setBanned(bannedIps)
			.setQuestionFactory(questionFactory)
			.setQuestions(selectables)
			.setTips(tips);
		//@formatter:on
		return builder;
	}
}
