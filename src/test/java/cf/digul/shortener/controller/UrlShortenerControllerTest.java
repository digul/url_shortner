package cf.digul.shortener.controller;

import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cf.digul.shortener.controller.UrlShorenerController;
import cf.digul.shortener.vo.Url;
import cf.digul.shortener.service.UrlShortenerService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UrlShorenerController.class)
@ActiveProfiles("!test")
public class UrlShortenerControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UrlShortenerService service;
	

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

	private ObjectMapper mapper = new ObjectMapper();

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
		.andExpect(MockMvcResultMatchers.content().string("Welcome"));
	}

	@Test
	public void testGetRealUrl() throws Exception {
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/" + sampleShortUrl)
				.accept(contentType))
				.andExpect(status().isOk()).andReturn();
		
		JSONAssert.assertEquals(mapper.writeValueAsString(sampleUrl)
				, result.getResponse().getContentAsString(), false);
		
	}
	
	@Test
	@Ignore("구현 후 테스트")
	public void testGetRealUrlUndefined() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/UndefUrl").accept(contentType))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void testGenerateShortUrl() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/gen")
				.content("something.new.url")
				.contentType(contentType))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", containsString("/gen/")))
				.andExpect(jsonPath("shortUrl").exists());
	}
	
	@Test
	public void testGenerateShortUrlAlreadyGen() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/gen")
				.content(sampleRealUrl)
				.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(jsonPath("shortUrl").value(sampleShortUrl));
	}
	
}
