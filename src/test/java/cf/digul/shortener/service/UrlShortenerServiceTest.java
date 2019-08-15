package cf.digul.shortener.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.test.annotation.IfProfileValue;

import cf.digul.shortener.vo.Url;
import cf.digul.shortener.repository.UrlRepository;
import cf.digul.shortener.util.ShortUrlGenerator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

import org.hamcrest.Matchers;

@RunWith(MockitoJUnitRunner.class)
@IfProfileValue(name = "unit-test", value = "true")
public class UrlShortenerServiceTest {
	
	@InjectMocks
	private UrlShortenerService service;
	
	@Mock
	private UrlRepository urlRepository;

	private static final String SAMPLE_REAL_URL = "sample.real.url.save";
	private static final String SAMPLE_SHORT_URL = "RepoTest";
	private static Url sampleUrl = new Url();
	
	@BeforeClass
	public static void setTestData() {
		sampleUrl.setRealUrl(SAMPLE_REAL_URL);
		sampleUrl.setShortUrl(SAMPLE_SHORT_URL);
		Whitebox.setInternalState(sampleUrl, "callCnt", 0);
		Whitebox.setInternalState(sampleUrl, "id", "1234");
	}
	
	@Before
	public void setMockRepository() {
		// sample로 테스트하면 탐색성공, 다른 url로는 탐색실패
		when(urlRepository.findOneByRealUrl(eq(SAMPLE_REAL_URL))).thenReturn(sampleUrl);
		when(urlRepository.findOneByRealUrl(not(eq(SAMPLE_REAL_URL)))).thenReturn(null);
		when(urlRepository.findAndCountByShortUrl(eq(SAMPLE_SHORT_URL))).thenReturn(sampleUrl);
		when(urlRepository.findAndCountByShortUrl(not(eq(SAMPLE_SHORT_URL)))).thenReturn(null);
		
		when(urlRepository.save(any())).thenReturn(new Url());
	}

	@Test
	public void testFindUrl() {
		assertNotNull(service.findUrl(SAMPLE_SHORT_URL));
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
		
	}
	
	@Test
	public void testGenerateShortUrl() {
		String sampleId = "5d542e230f2f459420dbbea1";
		assertThat(ShortUrlGenerator.encode(sampleId).length(), Matchers.lessThanOrEqualTo(8));	// 8자 이하여야 함 
	}
	
	@Test
	public void testSaveUrlAlreadyGen() {
		Url result = service.saveUrl(SAMPLE_REAL_URL);
		assertNotNull(result);
		assertFalse(result.isNew());	// 이미 생성된 url
		
	}
}
