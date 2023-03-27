package com.uber.driveronboardingmanagement.interfaces.configuration.logging;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(1)
public class RequestTracingFilter implements Filter {
	private final static String REQUEST_ID_KEY = "requestId";
	private final Random rand = new Random();

	private String createRequestId() {
		return Integer.toString(rand.nextInt(9999));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		MDC.put(REQUEST_ID_KEY, createRequestId());
		chain.doFilter(request, response);
	}
}
