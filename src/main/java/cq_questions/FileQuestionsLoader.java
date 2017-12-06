package cq_questions;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.compress.utils.IOUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileQuestionsLoader {
	private static final Logger LOG = LoggerFactory.getLogger(FileQuestionsLoader.class);

	private final Random generator;

	private final String questionsFile;

	private final String encoding;

	private final String password;

	public FileQuestionsLoader(final String questionsFile, final String encoding, final String password) {
		this.questionsFile = questionsFile;
		this.encoding = encoding;
		this.password = password;
		this.generator = new Random();
	}

	public Questions load() {
		try {
			final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(this.password);
			final URL url = this.getClass().getClassLoader().getResource(this.questionsFile);
			if (url != null) {
				Questions allQuestions;
				final byte[] encoded = IOUtils.toByteArray(url.openStream());
				final String encodedText = new String(encoded);
				final String decoded = textEncryptor.decrypt(encodedText);
				final String questionsFileDecoded = UUID.randomUUID().toString();
				try (OutputStream os = new FileOutputStream(questionsFileDecoded)) {
					os.write(decoded.getBytes(Charset.forName(this.encoding)));
				}
				try (
					BufferedReader in = new BufferedReader(
							new InputStreamReader(new FileInputStream(questionsFileDecoded), this.encoding))) {
					allQuestions = this.readQuestions(in);
				}
				//Files.delete(Paths.get(questionsFileDecoded));
				LOG.info("questions loaded");
				return allQuestions;
			} else
				LOG.info("question file not found");
		} catch (final IOException e) {
			LOG.error(e.getMessage());
		}
		return new Questions(new ArrayList<RawSelectable>(), new ArrayList<RawTip>());
	}

	private Questions readQuestions(final BufferedReader in) throws IOException {
		final List<RawSelectable> rawQuestions = new ArrayList<>();
		final List<RawTip> rawTips = new ArrayList<>();
		for (String inputLine = in.readLine(); inputLine != null; inputLine = in.readLine()) {
			final String[] question = inputLine.split("\\|");
			if (question.length == 5) {
				final String[] options = new String[4];
				System.arraycopy(question, 1, options, 0, 4);
				final int trueAnswer = this.shuffle(options);
				rawQuestions.add(new RawSelectable(question[0], options, trueAnswer));
			}
			if (question.length == 2)
				rawTips.add(new RawTip(question[0], Integer.parseInt(question[1])));
		}
		return new Questions(rawQuestions, rawTips);
	}

	private int shuffle(final String[] array) {
		final String trueAnswer = array[0];
		for (int i = array.length - 1; i > 0; i--) {
			final int index = this.generator.nextInt(i + 1);
			final String a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(trueAnswer))
				return i + 1;
		return 0;
	}
}
