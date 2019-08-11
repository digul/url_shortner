package cf.digul.shortener.controller;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cf.digul.shortener.service.UrlShortenerService;
import cf.digul.shortener.vo.Url;


@RestController
public class MainController {
	private static final Logger logger = LogManager.getLogger(MainController.class);
	
	@Autowired
	private UrlShortenerService urlShortenerService;
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main() {
		logger.debug("##MainController## Main page called.");
		return "Welcome"; 
	}
	
	@RequestMapping(value = "/{shortUrl}", method = RequestMethod.GET)
	public Url getRealUrl(@PathVariable String shortUrl) {
		logger.debug(String.format("##MainController## 호출된 shortUrl : %s",shortUrl));

		Url url = urlShortenerService.findUrl(shortUrl);
		
		return url; 
	}
	
	@RequestMapping(value = "/gen", method = RequestMethod.POST)
	public ResponseEntity<?> generateShortUrl (@RequestBody String realUrl) {
		logger.debug(String.format("##MainController## 입력된 Real Url : %s", realUrl));
		
		Url generatedUrl = urlShortenerService.generateShortUrl(realUrl);
		if(generatedUrl == null) {
			logger.debug("##MainController## generation has failed");
			return ResponseEntity.noContent().build();
		}
		logger.debug(String.format("##MainController## 생성된 Short Url : %s", generatedUrl.getShortUrl()));
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{shortUrl}").buildAndExpand(generatedUrl.getShortUrl()).toUri();
		
		
		return ResponseEntity.created(location).build();
	}
}
