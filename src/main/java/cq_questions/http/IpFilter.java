package cq_questions.http;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(IpFilter.class);

	private final Set<String> bannedIps;

	private final Set<String> allowedIps;

	public IpFilter(Builder builder) {
		this.bannedIps = builder.bannedIps;
		this.allowedIps = builder.allowedIps;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		final String ip = servletRequest.getRemoteAddr();
		if (servletResponse instanceof HttpServletResponse) {
			final HttpServletResponse httpservletResponse = (HttpServletResponse) servletResponse;
			final boolean isIPAllowed = (this.allowedIps.isEmpty() || this.allowedIps.contains(ip))
					&& !this.bannedIps.contains(ip);
			if (isIPAllowed == false) {
				httpservletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
				LOG.info("banned ip {} filtered : ", ip);
			} else
				filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private Set<String> bannedIps;

		private Set<String> allowedIps;

		public Builder setBannedIps(Set<String> bannedIps) {
			this.bannedIps = bannedIps;
			return this;
		}

		public Builder setAllowedIps(Set<String> allowedIps) {
			this.allowedIps = allowedIps;
			return this;
		}
	}
}
