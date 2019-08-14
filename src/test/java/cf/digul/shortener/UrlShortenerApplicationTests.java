package cf.digul.shortener;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cf.digul.shortener.UrlShortenerApplication;
import cf.digul.shortener.repository.UrlRepository;
import cf.digul.shortener.vo.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerApplicationTests {

	private static final String LOCAL_HOST = "http://localhost:";
	private static final String SAMPLE_URL = "https://daum.net";
	
	@LocalServerPort
	private int port;
	
	private TestRestTemplate template = new TestRestTemplate();

	@Autowired
	public UrlRepository repository;	// 데이터클리어용
	
	@Before
	public void clearTestData() {
		repository.deleteByRealUrl(SAMPLE_URL);
	}
	@Test
	public void welcome() throws Exception {
		ResponseEntity<String> response = template.getForEntity(createURL("/"), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetRealUrlUndefined() throws Exception {
		ResponseEntity<String> response = template.getForEntity(createURL("/UndefUrl"), String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testGenerateAndGetUrl() throws Exception {
		ResponseEntity<Url> generatedResponse 
			= template.postForEntity(createURL("/"), SAMPLE_URL, Url.class);
		
		assertEquals(HttpStatus.CREATED, generatedResponse.getStatusCode());
		assertTrue(generatedResponse.hasBody());
		
		Url generatedEntity = generatedResponse.getBody();
		assertThat(generatedEntity.getShortUrl().length(), Matchers.lessThanOrEqualTo(8));	// 8자이하
		
		ResponseEntity<String> foundResponse = template.getForEntity(createURL("/" + generatedEntity.getShortUrl()), String.class);
		assertEquals(HttpStatus.FOUND, foundResponse.getStatusCode());	// 생성된 shortUrl을 호출하면 최초의 realUrl로 리다이렉트됨
		assertEquals(SAMPLE_URL, foundResponse.getHeaders().getLocation().toString());
	}


	@Test
	public void testGenerateDuplicate() throws Exception {
		template.postForEntity(createURL("/"), SAMPLE_URL, Url.class);
		
		ResponseEntity<Url> secondResponse 
			= template.postForEntity(createURL("/"), SAMPLE_URL, Url.class);
		// 같은 url로 두번 생성시도
		
		assertEquals(HttpStatus.OK, secondResponse.getStatusCode());	// 응답은 한다
		assertTrue(secondResponse.hasBody());
		assertNotNull(secondResponse.getBody().getShortUrl());	
		assertFalse(secondResponse.getBody().isNew());					// is not new로 응답.
	}
	
	
	@Test
	public void testGenerateInvalidUrl() {
		// 유효하지 않은 url로는 short url을 만들 수 없다.
		String[] invalidStrings = {
				"http://invalid.real.url",
				"<script>",
				"eval(",
				"SOME STRING"
			};

		for(String invalidString : invalidStrings) {
			ResponseEntity<Url> invalidRes 
				= template.postForEntity(createURL("/"), invalidString, Url.class);
			assertEquals(HttpStatus.METHOD_NOT_ALLOWED, invalidRes.getStatusCode());	
		}
	}
	
	private String createURL(String uri) {
		return LOCAL_HOST + port + uri;
	}	
}
