package cq_questions.factory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_questions.model.Option;
import cq_questions.model.Selectable;

public class FileSelectableLoader {
	private static final Logger LOG = LoggerFactory.getLogger(FileSelectableLoader.class);

	private final Random generator;

	private final String fileName;

	private final String encoding;

	public FileSelectableLoader(final String fileName, final String encoding) {
		this.fileName = fileName;
		this.encoding = encoding;
		this.generator = new Random();
	}

	private Option[] createOptions(final String[] rawOptions) {
		final Option[] options = new Option[rawOptions.length];
		for (int i = 0; i < rawOptions.length; i++)
			if (i == 0)
				options[0] = new Option(rawOptions[0], true);
			else
				options[i] = new Option(rawOptions[i], false);
		return options;
	}

	public Map<String, Selectable> load() {
		try (
			FileInputStream fis = new FileInputStream(this.fileName);
			InputStreamReader isr = new InputStreamReader(fis, this.encoding);
			BufferedReader in = new BufferedReader(isr)) {
			final Map<String, Selectable> questions = new HashMap<>();
			for (String inputLine = in.readLine(); inputLine != null; inputLine = in.readLine()) {
				final String[] parts = inputLine.split("\\|");
				if (parts.length == 7) {
					final String theme = parts[0];
					final String id = parts[1];
					final String text = parts[2];
					final String[] rawOptions = new String[4];
					System.arraycopy(parts, 3, rawOptions, 0, 4);
					final Option[] options = this.createOptions(rawOptions);
					this.shuffle(options);
					questions.put(id, new Selectable(theme, id, text, options));
				} else
					LOG.error("invalid line: {}", inputLine);
			}
			return Collections.unmodifiableMap(questions);
		} catch (final IOException e) {
			LOG.error("file not found: ", e);
		}
		return Collections.emptyMap();
	}

	private void shuffle(final Option[] options) {
		for (int i = options.length - 1; i > 0; i--) {
			final int index = this.generator.nextInt(i + 1);
			final Option temp = options[index];
			options[index] = options[i];
			options[i] = temp;
		}
	}
}
