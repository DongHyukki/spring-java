package com.donghyukki.springjava.support.common.rest;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    public static final String HEADER_TRACE_ID = "X-B3-TraceId";
    public static final String HEADER_SPAN_ID = "X-B3-SpanId";
    public static final String MDC_TRACE_ID_KEY = "traceId";
    public static final String MDC_SPAN_ID_KEY = "spanId";

    private final ClientHttpRequestInterceptor interceptors = (request, body, execution) -> {
        var headers = request.getHeaders();

        if (!headers.containsKey(HEADER_TRACE_ID)) {
            headers.set(HEADER_TRACE_ID, MDC.get(MDC_TRACE_ID_KEY));
        }
        if (!headers.containsKey(HEADER_SPAN_ID)) {
            headers.set(HEADER_SPAN_ID, MDC.get(MDC_SPAN_ID_KEY));
        }

        return execution.execute(request, body);
    };

    @Bean
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(interceptors));
        return restTemplate;
    }
}
