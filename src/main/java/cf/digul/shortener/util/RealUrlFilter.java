package cf.digul.shortener.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@WebFilter(urlPatterns = "/")
public class RealUrlFilter implements Filter{
	
	private static final Logger logger = LogManager.getLogger(RealUrlFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if(!(request instanceof HttpServletRequest)) {
			return;
		}
		if(!(response instanceof HttpServletResponse)) {
			return;
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		if(!httpRequest.getMethod().equals(HttpMethod.POST.toString())) {
			chain.doFilter(request, httpResponse);
			return;	// POST 요청에 대해서만 처리
		}

		logger.debug("## FILTER ## RequestWrapper to CONTROLLER.generateShortUrl() ");
		RequestWrapper wrappedRequest = new RequestWrapper(httpRequest);
		
		if(isRealUrlValid(wrappedRequest.getBody())) {
			chain.doFilter(wrappedRequest, httpResponse);
		} else {
			httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}	
	}
	
	private boolean isRealUrlValid(String realUrl) {
		logger.debug("## FILTER ## CHECK IF valid URL : " + realUrl);
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> result = null;
		
		try {
			result = template.getForEntity(realUrl, String.class);
		} catch(Exception e) {
			logger.debug("## FILTER ## invalid url");
			return false;
		}
		
		if(result.getStatusCode().is3xxRedirection() || result.getStatusCode().is2xxSuccessful()) {
			logger.debug("## FILTER ## valid url");
			return true;
		}
		logger.debug("## FILTER ## invalid url");
		return false;
	}
}
