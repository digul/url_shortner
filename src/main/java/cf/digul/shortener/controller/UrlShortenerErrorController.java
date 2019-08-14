package cf.digul.shortener.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cf.digul.shortener.vo.DefinedException;

@Controller
public class UrlShortenerErrorController implements ErrorController{
	private static final Logger logger = LogManager.getLogger(UrlShortenerErrorController.class);

	private static final String ERROR_PATH = "/error";
	
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
	@RequestMapping(value = ERROR_PATH, method = RequestMethod.GET)
	public String errorPage(HttpServletRequest request, Model model) throws DefinedException {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errMessage;
        
        if( statusCode == null) {
        	throw new DefinedException("FAKE ERROR HAS CALLED");
        }
        
        String errorCode = statusCode.toString();
        
        if( request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) != null ) {
        	Exception ex = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        	if(ex.getCause() instanceof DefinedException) {
        		errMessage = ex.getCause().getLocalizedMessage();
        	} else {
        		errMessage = "UNKNOWN SERVER ERROR";
        	}
        } else {
        	errMessage = HttpStatus.valueOf(Integer.valueOf(errorCode)).name();
        }
		logger.debug(String.format("##ErrorController## %s Error : %s", errorCode, errMessage));
		
        model.addAttribute("code", errorCode);
        model.addAttribute("message", errMessage);
        
		return "error";
	}
}
