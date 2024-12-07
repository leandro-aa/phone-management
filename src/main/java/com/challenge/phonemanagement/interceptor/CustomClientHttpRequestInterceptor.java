package com.challenge.phonemanagement.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CustomClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request);
        var response = execution.execute(request, body);
        return logResponse(response);
    }

    private void logRequest(HttpRequest request) {
        log.debug("Request: {} {}", request.getMethod(), request.getURI());
    }

    private ClientHttpResponse logResponse(ClientHttpResponse response) throws IOException {
        log.debug("Response status: {}", response.getStatusCode());
        return response;
    }
}
