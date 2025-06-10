package com.zerobase.springjava.support.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Map;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        var requestWrapper = new ContentCachingRequestWrapper(request);
        var responseWrapper = new ContentCachingResponseWrapper(response);
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        System.out.println("MDC Map = " + contextMap);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String requestBody = new String(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = new String(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

        LOGGER.info("Request Body: {}", requestBody);
        LOGGER.info("Response Body: {}", responseBody);

        responseWrapper.copyBodyToResponse();
    }
}
