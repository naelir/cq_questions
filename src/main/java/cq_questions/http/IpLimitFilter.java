package cq_questions.http;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class IpLimitFilter implements Filter {
	private static final String[] LIMITED_PATHS = new String[] { "/" };

	private final IpTimeWindowManager ipTimeWindowManager;

	public IpLimitFilter(final IpTimeWindowManager ipTimeWindowManager) {
		this.ipTimeWindowManager = ipTimeWindowManager;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = this.getHttpServletRequest(servletRequest);
		final boolean isRestServicePostCall = this.isRestPublicUserServicePostCall(request);
		if (isRestServicePostCall) {
			final String ipAddress = request.getRemoteAddr();
			this.ipTimeWindowManager.addIpRequest(ipAddress);
			if (this.ipTimeWindowManager.ipAddressReachedLimit(ipAddress))
				return;
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private HttpServletRequest getHttpServletRequest(final ServletRequest servletRequest) {
		if (servletRequest instanceof HttpServletRequest)
			return (HttpServletRequest) servletRequest;
		else {
			final Object springRequest = servletRequest
					.getAttribute("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES");
			if (springRequest instanceof HttpServletRequest)
				return (HttpServletRequest) springRequest;
			else
				return null;
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	private boolean isRestPublicUserServicePostCall(final HttpServletRequest request) {
		final String requestedUri = request.getRequestURI();
		return Arrays.stream(LIMITED_PATHS).anyMatch(limitedPath -> requestedUri.startsWith(limitedPath));
	}
}