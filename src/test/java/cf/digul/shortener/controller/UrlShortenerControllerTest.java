package cf.digul.shortener.controller;

import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import cf.digul.shortener.controller.UrlShorenerController;
import cf.digul.shortener.vo.Url;
import cf.digul.shortener.service.UrlShortenerService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UrlShorenerController.class)
@IfProfileValue(name = "unit-test", value = "true")
public class UrlShortenerControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UrlShortenerService service;
	

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

	private final String sampleRealUrl = "test.real.url";
	private final String sampleShortUrl = "aAbBcCdD";
	private Url sampleUrl = new Url(sampleRealUrl, sampleShortUrl);
	
	@Before
	public void setMockBean() {
		sampleUrl.setIsNew(false);
		when(service.findUrl(eq(sampleShortUrl))).thenReturn(sampleUrl);	// 탐색성공
		when(service.findUrl(not(eq(sampleShortUrl)))).thenReturn(null);	// 탐색실패
		when(service.saveUrl(eq(sampleRealUrl))).thenReturn(sampleUrl);	// 이미 생성된 url을 저장시도
		when(service.saveUrl(not(eq(sampleRealUrl)))).thenReturn(new Url("something.new.url","NewUrl"));	// 새로운 url저장
	}
	
	@Test
	public void testMain() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(contentType))
		.andExpect(status().isOk())
		.andExpect(forwardedUrl("/views/main.jsp"));
	}

	@Test
	public void testGetRealUrl() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/" + sampleShortUrl)
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
				.content(sampleRealUrl)
				.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(jsonPath("shortUrl").value(sampleShortUrl));	// 이미 생성된 short url
	}
	
}
