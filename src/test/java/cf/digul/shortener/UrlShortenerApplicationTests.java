package cf.digul.shortener;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import cf.digul.shortener.UrlShortenerApplication;
import cf.digul.shortener.vo.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerApplicationTests {

	private static final String LOCAL_HOST = "http://localhost:";
	
	@LocalServerPort
	private int port;
	
	private TestRestTemplate template = new TestRestTemplate();
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void welcome() throws Exception {
		ResponseEntity<String> response = template.getForEntity(createURL("/"), String.class);
		
		assertThat(response.getBody(), equalTo("Welcome"));
	}
	
	@Test
	public void getRealUrl() throws Exception {
		Url sampleUrl = new Url("test.real.url", "aAbBcCdD");
		
		ResponseEntity<String> response = template.getForEntity(createURL("/" + sampleUrl.getShortUrl()), String.class);

		JSONAssert.assertEquals(mapper.writeValueAsString(sampleUrl)
				, response.getBody(), false);
	}
	
	@Test
	public void generateShortUrl() throws Exception {
		Url sampleUrl = new Url("test.real.url", "aAbBcCdD");
		
		URI location = template.postForLocation(createURL("/gen"), sampleUrl.getRealUrl());
		
		assertThat(location.getPath(), containsString("/gen/aAbBcCdD"));
		
	}
	
	private String createURL(String uri) {
		return LOCAL_HOST + port + uri;
	}

}
