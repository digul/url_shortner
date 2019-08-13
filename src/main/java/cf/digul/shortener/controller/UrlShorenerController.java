package cf.digul.shortener.controller;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cf.digul.shortener.vo.Url;
import cf.digul.shortener.service.UrlShortenerService;


@Controller
@RequestMapping(value = "/")
public class UrlShorenerController {
	private static final Logger logger = LogManager.getLogger(UrlShorenerController.class);
	
	@Autowired
	private UrlShortenerService urlShortenerService;
	

	@RequestMapping(method = RequestMethod.GET)
	public String main() {
		logger.debug("##Controller## Main page called.");
		return "main"; 
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> generateShortUrl (@RequestBody String realUrl) {
		logger.debug(String.format("##Controller.generateShortUrl## 입력된 Real Url : %s", realUrl));
		
		Url generatedUrl = urlShortenerService.saveUrl(realUrl);
		if(generatedUrl == null) {
			logger.debug("##Controller.generateShortUrl## generation has failed");
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{shortUrl}").buildAndExpand(generatedUrl.getShortUrl()).toUri();
		
		if(!generatedUrl.isNew()) {
			logger.debug(String.format("##Controller.generateShortUrl## this url is already generated : %s", generatedUrl.getShortUrl()));
			return ResponseEntity.ok(generatedUrl);
		}
		
		logger.debug(String.format("##Controller.generateShortUrl## 생성된 Short Url : %s", generatedUrl.getShortUrl()));
	
		return ResponseEntity.created(location).body(generatedUrl);
	}
	
	@RequestMapping(value = "/{shortUrl}", method = RequestMethod.GET)
	public void getRealUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
		logger.debug(String.format("##Controller.getRealUrl## 호출된 shortUrl : %s",shortUrl));
		//TODO shortURl에 대한 injection, xss 체크
		
		Url url = urlShortenerService.findUrl(shortUrl);
		if(url == null) {
			logger.info("##Controller.getRealUrl## 생성되지 않은 shorturl 호출 시도 : " + shortUrl);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		logger.debug("##Controller.getRealUrl## REDIRECT -> " + url.getRealUrl());
		response.sendRedirect(url.getRealUrl());
	}
}
