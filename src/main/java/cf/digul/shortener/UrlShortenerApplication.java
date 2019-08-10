package cf.digul.shortener;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class UrlShortenerApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(UrlShortenerApplication.class, args);
	}

}
