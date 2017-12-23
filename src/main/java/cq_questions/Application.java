package cq_questions;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cq_questions.factory.DefaultQuestionFactory;
import cq_questions.factory.FileSelectableLoader;
import cq_questions.factory.FileTipLoader;
import cq_questions.factory.IQuestionFactory;
import cq_questions.http.Controller;
import cq_questions.model.Selectable;
import cq_questions.model.Tipable;

@SpringBootApplication
public class Application {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final String SELECTABLES_FILE = "selectables.txt";

	private static final String TIPABLES_FILE = "tipables.txt";

	public static void main(final String[] args) {
		LOG.info("cq questions starting");
		final SpringApplication application = new SpringApplication(Application.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.setWebEnvironment(true);
		application.run(args);
	}

	private final Builder builder;

	public Application() {
		try {
			new File(SELECTABLES_FILE).createNewFile();
			new File(TIPABLES_FILE).createNewFile();
		} catch (final IOException e) {
			LOG.error("error when creating needed files: ", e);
		}
		final Set<String> allowedIps = new HashSet<>();
		final Set<String> bannedIps = new HashSet<>();
		final Map<String, Selectable> selectables = new FileSelectableLoader(SELECTABLES_FILE,
				DEFAULT_ENCODING).load();
		final Map<String, Tipable> tipables = new FileTipLoader(TIPABLES_FILE, DEFAULT_ENCODING)
				.load();
		LOG.debug("loaded {} selectables, {} tipables", selectables.size(), tipables.size());
		final IQuestionFactory questionFactory = new DefaultQuestionFactory(tipables, selectables);
		//@formatter:off
		final Controller.Builder controllerBuilder = new Controller.Builder()
				.setAlllowedIps(allowedIps)
				.setBannedIps(bannedIps)
				.setQuestionFactory(questionFactory)
				.setSelectables(selectables)
				.setTipables(tipables);
		//@formatter:on
		this.builder = new Builder().setControllerBuilder(controllerBuilder);
	}

	private static final class Builder {
		cq_questions.http.Controller.Builder controllerBuilder;

		public Builder setControllerBuilder(Controller.Builder controllerBuilder) {
			this.controllerBuilder = controllerBuilder;
			return this;
		}
	}

	@Bean
	public cq_questions.http.Controller.Builder controllerBuilder() {
		return this.builder.controllerBuilder;
	}
}
