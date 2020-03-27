package com.learning.microservices.netflixzuulapigatewayserver;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class ZuulLoggingFilter extends ZuulFilter{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Object run() throws ZuulException {
		// actual logic to execute
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		logger.info("requst -> {} request url -> {}",request, request.getRequestURI());
		return null;
	}

	@Override
	public boolean shouldFilter() {
		// should execute or not
		return true;
	}

	@Override
	public int filterOrder() {
		// order in which filters should execute 
		return 1;
	}

	@Override
	public String filterType() {
		// when to execute - pre, post, error
		return "pre";
	}

}
