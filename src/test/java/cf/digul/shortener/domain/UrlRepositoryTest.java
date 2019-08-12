package cf.digul.shortener.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cf.digul.shortener.document.UrlDocument;
import cf.digul.shortener.document.UrlRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class UrlRepositoryTest {
	
	private final String sampleRealUrl = "test.real.url";
	private final String sampleShortUrl = "aAbBcCdD";

	@Autowired
	UrlRepository repository;
	
	@Before
	public void saveSampleData() {
		UrlDocument url = new UrlDocument();
		url.setRealUrl(this.sampleRealUrl);
		url.setShortUrl(this.sampleShortUrl);
		if(repository.findAll().size() == 0) {
			repository.save(url);
		}
	}
	
	@Test
	public void testFindByRealUrl() {		
		UrlDocument resultUrl = repository.findByRealUrl(this.sampleRealUrl);
		assertEquals(this.sampleRealUrl, resultUrl.getRealUrl());
		assertEquals(this.sampleShortUrl, resultUrl.getShortUrl());
		assertEquals(0, resultUrl.getCallCnt());
	}	
	
	@Test
	public void testFindByShortUrl() {
		UrlDocument resultUrl = repository.findByShortUrl(this.sampleShortUrl);
		assertEquals(this.sampleRealUrl, resultUrl.getRealUrl());
		assertEquals(this.sampleShortUrl, resultUrl.getShortUrl());
		assertEquals(0, resultUrl.getCallCnt());
	}
	
	@Test
	public void testFindAndCountByShortUrl() {
		UrlDocument resultUrl = repository.findAndCountByShortUrl(this.sampleShortUrl);
		assertEquals(this.sampleRealUrl, resultUrl.getRealUrl());
		assertEquals(this.sampleShortUrl, resultUrl.getShortUrl());
		assertEquals(1, resultUrl.getCallCnt());
	}
}
