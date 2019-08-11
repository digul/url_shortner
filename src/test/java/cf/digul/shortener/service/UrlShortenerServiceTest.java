package cf.digul.shortener.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import cf.digul.shortener.vo.Url;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UrlShortenerServiceTest {
	
	@InjectMocks
	private UrlShortenerService service;
	
	@Mock
	private Url mockUrl;


	@Test
	public void testFindUrl() {
		when(mockUrl.getShortUrl()).thenReturn("aAbBcCdD");
		when(mockUrl.getRealUrl()).thenReturn("test.real.url");
		
		Url url = service.findUrl(mockUrl.getShortUrl());
		assertEquals(url.getRealUrl(), mockUrl.getRealUrl());
		assertEquals(url.getShortUrl(), mockUrl.getShortUrl());
	}
	
	@Test
	@Ignore("실패응답 구현 후 테스트")
	public void testFindUrlFail() {
		when(mockUrl.getShortUrl()).thenReturn(anyString());
		
		Url url = service.findUrl(mockUrl.getShortUrl());
		//TODO 실패응답리턴
	}
	
	@Test
	public void testGenerateShortUrl() {
		when(mockUrl.getShortUrl()).thenReturn("aAbBcCdD");
		when(mockUrl.getRealUrl()).thenReturn("test.real.url");
		
		Url url = service.generateShortUrl(mockUrl.getRealUrl());

		assertEquals(url.getRealUrl(), mockUrl.getRealUrl());
		assertThat(url.getShortUrl().length(), lessThanOrEqualTo(8));
		// 8자 이하여야 함

		Url url2 = service.generateShortUrl(mockUrl.getRealUrl());
		assertEquals(url.getShortUrl(), url2.getShortUrl());
		// 같은 real url이 같은 short url을 리턴해야 함
	}
	
	@Test
	@Ignore("injection 방지 로직 구현 후 테스트")
	public void testGenerateShortUrlByWrongString() {
		when(mockUrl.getRealUrl()).thenReturn("or 1=1");
		//TODO 오류응답 리턴
	}
	
}
