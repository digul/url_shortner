package cf.digul.shortener;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cf.digul.shortener.UrlShortenerApplication;
import cf.digul.shortener.config.MongoConfig;
import cf.digul.shortener.vo.Url;

@Ignore("embeded mongodb를 사용하도록 수정 후 테스트 필요")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DataMongoTest( excludeAutoConfiguration = MongoConfig.class)
public class UrlShortenerApplicationTests {

	private static final String LOCAL_HOST = "http://localhost:";
	
	@LocalServerPort
	private int port;
	
	private TestRestTemplate template = new TestRestTemplate();
	
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
	public void testGenerateAndGetUrl() throws Exception {
		String sampleRealUrl = "sample.real.url";
		ResponseEntity<Url> generatedResponse 
			= template.postForEntity(createURL("/gen"), sampleRealUrl, Url.class);
		
		assertEquals(generatedResponse.getStatusCode(), HttpStatus.CREATED);
		assertTrue(generatedResponse.hasBody());
		
		Url generatedEntity = generatedResponse.getBody();
		assertThat(generatedEntity.getShortUrl().length(), Matchers.lessThanOrEqualTo(8));	// 8자이하
		
		ResponseEntity<Url> foundResponse = template.getForEntity(createURL("/" + generatedEntity.getShortUrl()), Url.class);
		assertEquals(foundResponse.getStatusCode(), HttpStatus.OK);
		assertTrue(foundResponse.hasBody());
		assertEquals(foundResponse.getBody().getRealUrl(), sampleRealUrl);	// 생성된 shortUrl을 호출하면 최초의 realUrl이 리턴됨
	}


	@Test
	public void testGenerateDuplicate() throws Exception {
		String sampleRealUrl = "sample.real.url.dup";
		
		template.postForEntity(createURL("/gen"), sampleRealUrl, Url.class);
		
		ResponseEntity<Url> secondResponse 
			= template.postForEntity(createURL("/gen"), sampleRealUrl, Url.class);
		// 같은 url로 두번 생성시도
		
		assertTrue(secondResponse.hasBody());
		assertEquals(secondResponse.getStatusCode(), HttpStatus.OK);	
		assertNotNull(secondResponse.getBody().getShortUrl());		// 응답은 한다
		assertFalse(secondResponse.getBody().isNew());				// is not new로 응답.
		
	}
	
	private String createURL(String uri) {
		return LOCAL_HOST + port + uri;
	}

}
