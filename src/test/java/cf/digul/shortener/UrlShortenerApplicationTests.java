package cf.digul.shortener;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cf.digul.shortener.UrlShortenerApplication;
import cf.digul.shortener.controller.UrlShortenerController;
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
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetRealUrlUndefined() throws Exception {
		ResponseEntity<Url> response = template.getForEntity(createURL("/UndefUrl"), Url.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testGetRealUrlInvalid() throws Exception {
		for(String invalidString : invalidStrings) {
			ResponseEntity<Url> invalidRes 
				= template.getForEntity(createURL("/" + invalidString), Url.class);
			assertEquals(HttpStatus.FORBIDDEN, invalidRes.getStatusCode());	
		}
	}
	
	@Test
	public void testGenerateAndGetUrl() throws Exception {
		ResponseEntity<Url> generatedResponse 
			= template.postForEntity(createURL("/"), SAMPLE_URL, Url.class);
		
		assertEquals(HttpStatus.CREATED, generatedResponse.getStatusCode());
		assertTrue(generatedResponse.hasBody());
		
		Url generatedEntity = generatedResponse.getBody();
		assertThat(generatedEntity.getShortUrl().length(), Matchers.lessThanOrEqualTo(8));	// 8자이하
		
		ResponseEntity<Url> foundResponse = template.getForEntity(createURL("/" + generatedEntity.getShortUrl()), Url.class);
		assertEquals(HttpStatus.OK, foundResponse.getStatusCode());
		assertTrue(foundResponse.hasBody());
		assertEquals(foundResponse.getBody().getRealUrl(), SAMPLE_URL);	// 생성된 shortUrl을 호출하면 최초의 realUrl이 리턴됨
	}


	@Test
	public void testGenerateDuplicate() throws Exception {
		template.postForEntity(createURL("/"), SAMPLE_URL, Url.class);
		
		ResponseEntity<Url> secondResponse 
			= template.postForEntity(createURL("/"), SAMPLE_URL, Url.class);
		// 같은 url로 두번 생성시도
		
		assertTrue(secondResponse.hasBody());
		assertEquals(HttpStatus.OK, secondResponse.getStatusCode());	
		assertNotNull(secondResponse.getBody().getShortUrl());		// 응답은 한다
		assertFalse(secondResponse.getBody().isNew());				// is not new로 응답.
		
	}
	
	@Test
	public void testGenerateInvalidUrl() {
		ResponseEntity<Url> response 
			= template.postForEntity(createURL("/"), "invalid.real.url", Url.class);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		
		for(String invalidString : invalidStrings) {
			ResponseEntity<Url> invalidRes 
				= template.postForEntity(createURL("/"), invalidString, Url.class);
			assertEquals(HttpStatus.FORBIDDEN, invalidRes.getStatusCode());	
		}
	}
	
	private String createURL(String uri) {
		return LOCAL_HOST + port + uri;
	}
}
