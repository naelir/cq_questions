package cq_questions;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class PostTest {
	private static final Logger LOG = LoggerFactory.getLogger(PostTest.class);

	public static void main(final String[] args) {
		Unirest.setObjectMapper(new ObjectMapper() {
			private final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

			@Override
			public <T> T readValue(final String value, final Class<T> valueType) {
				try {
					return this.jacksonObjectMapper.readValue(value, valueType);
				} catch (final IOException e) {
					LOG.error("{}", e);
					throw new RuntimeException(e);
				}
			}

			@Override
			public String writeValue(final Object value) {
				try {
					return this.jacksonObjectMapper.writeValueAsString(value);
				} catch (final JsonProcessingException e) {
					LOG.error("{}", e);
					throw new RuntimeException(e);
				}
			}
		});
		//@formatter:off
		try {
			final HttpResponse<String> postResponse = Unirest.post("http://localhost:8080/api/questions/add")
			        .header("accept", "application/json")
			        .header("Content-Type", "application/json")
			        .body(Arrays.asList(QuestionFactory.DEFAULT_QUESTION))
			        .asString();
			LOG.debug("{}", postResponse);
		} catch (final UnirestException e) {
			LOG.warn("{}", e);
		}
	}
}
