package cq_questions.http;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.google.common.collect.LinkedListMultimap;

public class IpTimeWindowManager {
	private static final int MAX_REQUEST_PER_IP_IN_WINDOW = 2;

	private static final int WINDOW_SIZE_IN_MINUTES = 300;

	private long lastEpochMinute;

	private final LinkedListMultimap<String, Long> requestsPerIp;

	public IpTimeWindowManager() {
		this.requestsPerIp = LinkedListMultimap.create();
		this.lastEpochMinute = 0;
	}

	public synchronized void addIpRequest(final String ipAddress) {
		final long epochSecond = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
		this.requestsPerIp.put(ipAddress, epochSecond);
		final long epochMinute = epochSecond - (epochSecond % 60);
		if (epochMinute > this.lastEpochMinute) {
			this.lastEpochMinute = epochMinute;
			this.cleanExpiredRequests();
		}
	}

	private void cleanExpiredRequests() {
		final long expiredEpochMinute = this.lastEpochMinute - (WINDOW_SIZE_IN_MINUTES * 60);
		for (final String ipAddress : this.requestsPerIp.keySet()) {
			final List<Long> requests = this.requestsPerIp.get(ipAddress);
			for (final Long request : requests)
				if (request < expiredEpochMinute)
					requests.remove(request);
			if (requests.isEmpty())
				this.requestsPerIp.removeAll(ipAddress);
		}
	}

	public synchronized boolean ipAddressReachedLimit(final String ipAddress) {
		final int amountRequests = this.requestsPerIp.get(ipAddress).size();
		final boolean value = amountRequests > MAX_REQUEST_PER_IP_IN_WINDOW;
		return value;
	}
}
