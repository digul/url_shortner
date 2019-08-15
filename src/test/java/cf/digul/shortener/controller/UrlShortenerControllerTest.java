package cf.digul.shortener.controller;

import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import cf.digul.shortener.controller.UrlShortenerController;
import cf.digul.shortener.vo.Url;
import cf.digul.shortener.service.UrlShortenerService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UrlShortenerController.class)
@IfProfileValue(name = "unit-test", value = "true")
public class UrlShortenerControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UrlShortenerService service;
	

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


	private static final String SAMPLE_REAL_URL = "sample.real.url.save";
	private static final String SAMPLE_SHORT_URL = "RepoTest";
	private static Url sampleUrl = new Url();
	private static Url newUrl = new Url();
	

	@BeforeClass
	public static void setTestData() {
		sampleUrl.setRealUrl(SAMPLE_REAL_URL);
		sampleUrl.setShortUrl(SAMPLE_SHORT_URL);
		sampleUrl.setIsNew(false);
		Whitebox.setInternalState(sampleUrl, "callCnt", 0);
		Whitebox.setInternalState(sampleUrl, "id", "1234");

		newUrl.setIsNew(true);
		newUrl.setShortUrl("NewUrl");
	}
	
	@Before
	public void setMockBean() {
		when(service.findUrl(eq(SAMPLE_SHORT_URL))).thenReturn(sampleUrl);	// 탐색성공
		when(service.findUrl(not(eq(SAMPLE_SHORT_URL)))).thenReturn(null);	// 탐색실패
		when(service.saveUrl(eq(SAMPLE_REAL_URL))).thenReturn(sampleUrl);	// 이미 생성된 url을 저장시도
		when(service.saveUrl(not(eq(SAMPLE_REAL_URL)))).thenReturn(newUrl);	// 새로운 url저장
	}
	
	@Test
	public void testMain() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(contentType))
		.andExpect(status().isOk())
		.andExpect(forwardedUrl("/WEB-INF/jsp/main.jsp"));
	}

	@Test
	public void testGetRealUrl() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/" + SAMPLE_SHORT_URL)
				.accept(contentType))
			.andExpect(status().is3xxRedirection());	// 생성된 shorturl -> redirect
	}
	
	@Test
	public void testGetRealUrlUndefined() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/UndefUrl")
				.accept(contentType))
			.andExpect(status().isNotFound());			// 생성되지 않은 shorturl -> 404 not found
	}
	
	@Test
	public void testGenerateShortUrl() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/")
				.content("something.new.url")
				.contentType(contentType))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("shortUrl").exists());	// short url 생성완료
	}
	
	@Test
	public void testGenerateShortUrlAlreadyGen() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/")
				.content(SAMPLE_REAL_URL)
				.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(jsonPath("shortUrl").value(SAMPLE_SHORT_URL));	// 이미 생성된 short url
	}	
}
