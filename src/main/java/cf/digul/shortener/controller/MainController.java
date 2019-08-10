package cf.digul.shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {
	private static final Logger logger = LogManager.getLogger(MainController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome() {
		logger.debug("log4j log test : debug");
		logger.info("log4j log test : info");
		logger.error("log4j log test : error");
		return "Welcome"; 
	}
}
