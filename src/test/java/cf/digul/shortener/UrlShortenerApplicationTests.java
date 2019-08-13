package cf.digul.shortener;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
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

@Ignore("jenkins test 일시적 회피")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerApplicationTests {

	private static final String LOCAL_HOST = "http://localhost:";
	private static final String SAMPLE_URL = "https://daum.net";
	
	@LocalServerPort
	private int port;
	
	private TestRestTemplate template = new TestRestTemplate();

	private String[] invalidStrings = {
			"or 1 = 1 --",
			"'having 1=1",
			"javascript:",
			"<script>",
			"eval("
			};


	@Autowired
	public UrlRepository repository;	// 데이터클리어용
	
	@Before
	public void clearTestData() {
		repository.deleteByRealUrl(SAMPLE_URL);
	}
	@Test
	public void welcome() throws Exception {
		ResponseEntity<String> response = template.getForEntity(createURL("/"), String.class);
		
		assertThat(response.getBody(), equalTo("Welcome"));
	}

	@Test
	public void testGetRealUrlUndefined() throws Exception {
		ResponseEntity<Url> response = template.getForEntity(createURL("/UndefUrl"), Url.class);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void testGetRealUrlInvalid() throws Exception {
		for(String invalidString : invalidStrings) {
			ResponseEntity<Url> invalidRes 
				= template.getForEntity(createURL("/" + invalidString), Url.class);
			assertEquals(invalidRes.getStatusCode(), HttpStatus.FORBIDDEN);	
		}
	}
	
	@Test
	public void testGenerateAndGetUrl() throws Exception {
		ResponseEntity<Url> generatedResponse 
			= template.postForEntity(createURL("/gen"), SAMPLE_URL, Url.class);
		
		assertEquals(generatedResponse.getStatusCode(), HttpStatus.CREATED);
		assertTrue(generatedResponse.hasBody());
		
		Url generatedEntity = generatedResponse.getBody();
		assertThat(generatedEntity.getShortUrl().length(), Matchers.lessThanOrEqualTo(8));	// 8자이하
		
		ResponseEntity<Url> foundResponse = template.getForEntity(createURL("/" + generatedEntity.getShortUrl()), Url.class);
		assertEquals(foundResponse.getStatusCode(), HttpStatus.OK);
		assertTrue(foundResponse.hasBody());
		assertEquals(foundResponse.getBody().getRealUrl(), SAMPLE_URL);	// 생성된 shortUrl을 호출하면 최초의 realUrl이 리턴됨
	}


	@Test
	public void testGenerateDuplicate() throws Exception {
		template.postForEntity(createURL("/gen"), SAMPLE_URL, Url.class);
		
		ResponseEntity<Url> secondResponse 
			= template.postForEntity(createURL("/gen"), SAMPLE_URL, Url.class);
		// 같은 url로 두번 생성시도
		
		assertTrue(secondResponse.hasBody());
		assertEquals(secondResponse.getStatusCode(), HttpStatus.OK);	
		assertNotNull(secondResponse.getBody().getShortUrl());		// 응답은 한다
		assertFalse(secondResponse.getBody().isNew());				// is not new로 응답.
		
	}
	
	@Test
	public void testGenerateInvalidUrl() {
		ResponseEntity<Url> response 
			= template.postForEntity(createURL("/gen"), "invalid.real.url", Url.class);
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
		
		for(String invalidString : invalidStrings) {
			ResponseEntity<Url> invalidRes 
				= template.postForEntity(createURL("/gen"), invalidString, Url.class);
			assertEquals(invalidRes.getStatusCode(), HttpStatus.FORBIDDEN);	
		}
	}
	
	private String createURL(String uri) {
		return LOCAL_HOST + port + uri;
	}
}
