package com.htnova.common.util.response;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class DelegatingResponseHandler implements ResponseHandler {

    private Map<RequestMatcher, ResponseHandler> handlers;

    private ResponseHandler defaultHandler;

    DelegatingResponseHandler(Map<RequestMatcher, ResponseHandler> handlers, ResponseHandler defaultHandler) {
        this.handlers = handlers;
        this.defaultHandler = defaultHandler;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        for (Map.Entry<RequestMatcher, ResponseHandler> entry : handlers.entrySet()) {
            RequestMatcher requestMatcher = entry.getKey();
            ResponseHandler handler = entry.getValue();
            if (requestMatcher.matches(request)) {
                handler.handle(request, response);
                return;
            }
        }
        defaultHandler.handle(request,response);
    }
}