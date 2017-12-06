package cq_questions;

import java.io.IOException;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(IpFilter.class);

	private final Set<String> bannedIps;

	private final Set<String> allowedIps;

	public IpFilter(final Set<String> bannedIps, final Set<String> allowedIps) {
		this.bannedIps = bannedIps;
		this.allowedIps = allowedIps;
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
				LOG.info("filter banned ip : " + ip);
			} else
				filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}
}
