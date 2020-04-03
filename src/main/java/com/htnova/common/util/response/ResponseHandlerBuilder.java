package com.htnova.common.util.response;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class ResponseHandlerBuilder{

    private HashMap<RequestMatcher, ResponseHandler> handlers = new LinkedHashMap<>();

    private ResponseHandler defaultHandler;

    public ResponseHandlerBuilder handlerFor(ResponseHandler handler, RequestMatcher preferredMatcher) {
        Assert.notNull(handler, "handler cannot be null");
        Assert.notNull(preferredMatcher, "preferredMatcher cannot be null");
        this.handlers.put(preferredMatcher, handler);
        return this;
    }

    public ResponseHandlerBuilder defaultHandler(ResponseHandler handler){
        Assert.notNull(handler, "handler cannot be null");
        this.defaultHandler = handler;
        return this;
    }

    public ResponseHandler build(){
        if(Objects.isNull(handlers) || handlers.isEmpty()){
            return defaultHandler;
        }else {
            return new DelegatingResponseHandler(handlers,defaultHandler);
        }
    }
}