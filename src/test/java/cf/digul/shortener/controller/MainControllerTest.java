package cf.digul.shortener.controller;

import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

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

import cf.digul.shortener.controller.MainController;
import cf.digul.shortener.service.UrlShortenerService;
import cf.digul.shortener.vo.Url;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UrlShortenerService service;
	

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void main() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(contentType))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.content().string("Welcome"));
	}

	@Test
	public void getRealUrl() throws Exception {
		Url sampleUrl = new Url("test.real.url", "aAbBcCdD");
		when(service.findUrl(anyString())).thenReturn(sampleUrl);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/aAbBcCdD").accept(contentType))
				.andExpect(status().isOk()).andReturn();
		
		JSONAssert.assertEquals(mapper.writeValueAsString(sampleUrl)
				, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void generateShortUrl() throws Exception {
		Url sampleUrl = new Url("test.real.url","aAbBcCdD");
		
		when(service.generateShortUrl(anyString())).thenReturn(sampleUrl);
		
		mvc.perform(MockMvcRequestBuilders.post("/gen")
				.content(sampleUrl.getRealUrl())
				.contentType(contentType))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", containsString("/gen/aAbBcCdD")));
	}
	
}
