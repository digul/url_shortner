package cf.digul.shortener;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import cf.digul.shortener.UrlShortenerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerApplicationTests {

	private static final String LOCAL_HOST = "http://localhost:";
	
	@LocalServerPort
	private int port;
	
	private TestRestTemplate template = new TestRestTemplate();
	
	@Test
	public void welcome() throws Exception {
		ResponseEntity<String> response = template.getForEntity(createURL("/"), String.class);
		
		Assert.assertThat(response.getBody(), equalTo("Welcome"));
	}
	
	@Test
	public void sampleUrl() throws Exception {
		String testUrl = "google.com";
		ResponseEntity<String> response = template.getForEntity(createURL("/url/" + testUrl), String.class);

		Assert.assertThat(response.getBody(), containsString(testUrl));
	}
	
	private String createURL(String uri) {
		return LOCAL_HOST + port + uri;
	}

}
