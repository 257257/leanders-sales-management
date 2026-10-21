
package com.leander.salesmanagement.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		logger.info("Incoming Request: Method={}, URI={}", request.getMethod(), request.getRequestURI());

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {

		logger.info("Completed Request: Method={}, URI={}, Status={}", request.getMethod(), request.getRequestURI(),
				response.getStatus());

		if (exception != null) {
			logger.error("Request failed with an exception", exception);
		}
	}
}
