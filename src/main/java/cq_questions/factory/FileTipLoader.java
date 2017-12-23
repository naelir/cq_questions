package cq_questions.factory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cq_questions.model.Tipable;

public class FileTipLoader {
	private static final Logger LOG = LoggerFactory.getLogger(FileTipLoader.class);

	private final String fileName;

	private final String encoding;

	public FileTipLoader(final String fileName, final String encoding) {
		this.fileName = fileName;
		this.encoding = encoding;
	}

	public Map<String, Tipable> load() {
		try (
			FileInputStream fis = new FileInputStream(this.fileName);
			InputStreamReader isr = new InputStreamReader(fis, this.encoding);
			BufferedReader in = new BufferedReader(isr)) {
			final Map<String, Tipable> tips = new HashMap<>();
			for (String inputLine = in.readLine(); inputLine != null; inputLine = in.readLine()) {
				final String[] parts = inputLine.split("\\|");
				if (parts.length == 4) {
					final String type = parts[0];
					final String id = parts[1];
					final String text = parts[2];
					tips.put(id, new Tipable(type, id, text, Integer.parseInt(parts[3])));
				} else
					LOG.error("invalid line: {}", inputLine);
			}
			return Collections.unmodifiableMap(tips);
		} catch (final IOException e) {
			LOG.error("file not found: ", e);
		}
		return Collections.emptyMap();
	}
}
