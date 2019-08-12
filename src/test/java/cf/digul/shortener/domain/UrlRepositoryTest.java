package cf.digul.shortener.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cf.digul.shortener.vo.Url;
import cf.digul.shortener.repository.UrlRepository;

import org.springframework.dao.DuplicateKeyException;

@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class UrlRepositoryTest {

	@Autowired
	public UrlRepository repository;
	
	@Test
	public void testSave() {	// Insert
		assertNotNull(repository.save(new Url("sample.real.url.save", "AABBCCDD")));
		assertNotNull(repository.save(new Url("sample2.real.url.save", "aabbccdd")));
	}

	@Test(expected = DuplicateKeyException.class)
	public void testSaveDuplicate() {
		repository.save(new Url("sample.real.url.savedup", "abc"));
		assertThat(repository.save(new Url("sample.real.url.savedup", "ABC")), is("realUrl중복"));
		assertThat(repository.save(new Url("sample2.real.url.savedup", "abc")), is("shortUrl중복"));
		
	}
	@Test
	public void testFindByRealUrl() {	
		String sampleRealUrl1 = "sample.real.url.findbyreal";
		String sampleRealUrl2 = "sample2.real.url.findbyreal";
		String sampleShortUrl1 = "EEFFGGHH";
		String sampleShortUrl2 = "eeffgghh";
		repository.save(new Url(sampleRealUrl1, sampleShortUrl1));
		repository.save(new Url(sampleRealUrl2, sampleShortUrl2));
		
		// 여기부터
		Url result = repository.findOneByRealUrl(sampleRealUrl1);
		assertEquals(sampleShortUrl1, result.getShortUrl());
		
		assertEquals(result.getShortUrl(), repository.findOneByRealUrl(sampleRealUrl1).getShortUrl());	// 동일 url로 동일데이터 find
		assertNotEquals(result.getShortUrl(), repository.findOneByRealUrl(sampleRealUrl2).getShortUrl());	// 다른 url로 다른 데이터 find
		
	}
	
	@Test
	public void testFindByRealUrlUndefined() {
		assertNull(repository.findOneByRealUrl("undefined.real.url"));
	}
	
	@Test
	public void testFindByShortUrl() {	
		String sampleRealUrl = "sample.real.url.findbyshort";
		String sampleShortUrl = "IIJJKKLLMM";
		repository.save(new Url(sampleRealUrl, sampleShortUrl));
		
		Url result = repository.findOneByShortUrl(sampleShortUrl);
		assertEquals(sampleRealUrl, result.getRealUrl());
	}
	
	@Test
	public void testFindByShortUrlUndefined() {
		assertNull(repository.findOneByShortUrl("Undef"));
	}
	
	@Test
	public void testFindAndCountByShortUrl() {
		String sampleRealUrl = "sample.real.url.findandcount";
		String sampleShortUrl = "nnooppqq";
		repository.save(new Url(sampleRealUrl, sampleShortUrl));
		
		Url result = repository.findAndCountByShortUrl(sampleShortUrl);
		assertEquals(sampleRealUrl, result.getRealUrl());
		
		assertEquals(result.getCallCnt() + 1, repository.findAndCountByShortUrl(sampleShortUrl).getCallCnt());	// 증가된 callCnt
	}
}
