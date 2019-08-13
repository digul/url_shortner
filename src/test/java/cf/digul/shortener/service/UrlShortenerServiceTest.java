package cf.digul.shortener.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import cf.digul.shortener.vo.Url;
import cf.digul.shortener.repository.UrlRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;
import org.hamcrest.Matchers;

@RunWith(MockitoJUnitRunner.Silent.class)
@ActiveProfiles("!test")
public class UrlShortenerServiceTest {
	
	@InjectMocks
	private UrlShortenerService service;
	
	@Mock
	private UrlRepository urlRepository;

	private String sampleRealUrl = "test.real.url";
	private String sampleShortUrl = "aAbBcCdD";
	
	@Before
	public void setMockRepository() {
		// sample로 테스트하면 탐색성공, 다른 url로는 탐색실패
		Url samplesUrl = new Url(sampleRealUrl, sampleShortUrl);
		when(urlRepository.findOneByRealUrl(eq(sampleRealUrl))).thenReturn(samplesUrl);
		when(urlRepository.findAndCountByShortUrl(eq(sampleShortUrl))).thenReturn(samplesUrl);
		when(urlRepository.findOneByRealUrl(not(eq(sampleRealUrl)))).thenReturn(null);
		when(urlRepository.findAndCountByShortUrl(not(eq(sampleShortUrl)))).thenReturn(null);
		
		when(urlRepository.save(any())).thenReturn(new Url("something.new.url","NewUrl"));
	}

	@Test
	public void testFindUrl() {
		assertNotNull(service.findUrl(sampleShortUrl));
	}
	
	@Test
	public void testFindUrlUndefined() {
		assertNull(service.findUrl("Undef"));
	}
	
	@Test
	public void testSaveUrl() {
		String sampleNewRealUrl = "test.new.real.url";
		
		Url result = service.saveUrl(sampleNewRealUrl);
		assertNotNull(result);
		assertThat(result.getShortUrl().length(), Matchers.lessThanOrEqualTo(8));	// 8자 이하여야 함
		assertTrue(result.isNew());	// 신규생성됨
	}
	
	@Test
	public void testSaveUrlAlreadyGen() {
		Url result = service.saveUrl(sampleRealUrl);
		assertNotNull(result);
		assertFalse(result.isNew());	// 이미 생성된 url
		
	}
}
