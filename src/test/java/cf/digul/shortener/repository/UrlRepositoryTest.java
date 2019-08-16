package cf.digul.shortener.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
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
	public void clearTestData() {
		repository.delete(sampleUrl);
	}
	
	@Test
	public void testSaveAndDelete() {
		Url savedUrl = repository.save(sampleUrl);
		assertNotNull(savedUrl.getId());
		
		repository.deleteByRealUrl(SAMPLE_REAL_URL);
		assertThat(repository.findById(savedUrl.getId())).isEmpty();
	}

	@Test(expected = DuplicateKeyException.class)
	public void testSaveDuplicate() {
		repository.save(sampleUrl);

		Url testUrl = new Url();
		Whitebox.setInternalState(testUrl, "id", "4321");
		Whitebox.setInternalState(testUrl, "callCnt", 0);
		
		testUrl.setRealUrl(SAMPLE_REAL_URL);
		testUrl.setShortUrl("ANOTHER");
		assertThat(repository.save(testUrl), is("realUrl중복"));

		testUrl.setRealUrl("another.real.url");
		testUrl.setShortUrl(SAMPLE_SHORT_URL);
		assertThat(repository.save(testUrl), is("shortUrl중복"));
		
	}
	@Test
	public void testFindByRealUrl() {
		repository.save(sampleUrl);
		Url anotherUrl = new Url("another.sample.url");
		anotherUrl.setShortUrl("TEST");
		anotherUrl = repository.save(anotherUrl);
		
		Url result = repository.findOneByRealUrl(SAMPLE_REAL_URL);
		assertEquals(result.getShortUrl(), repository.findOneByRealUrl(SAMPLE_REAL_URL).getShortUrl());	// 같은 url로 같은 데이터 find
		assertNotEquals(result.getShortUrl(), repository.findOneByRealUrl("another.sample.url").getShortUrl());	// 다른 url로 다른 데이터 find

		repository.delete(anotherUrl);
	}
	
	@Test
	public void testFindByRealUrlUndefined() {
		assertNull(repository.findOneByRealUrl("undefined.real.url"));
	}
	
	@Test
	public void testFindByShortUrl() {	
		repository.save(sampleUrl);
		
		Url result = repository.findOneByShortUrl(SAMPLE_SHORT_URL);
		assertEquals(SAMPLE_REAL_URL, result.getRealUrl());
	}
	
	@Test
	public void testFindByShortUrlUndefined() {
		assertNull(repository.findOneByShortUrl("Undef"));
	}
	
	@Test
	public void testFindAndCountByShortUrl() {
		repository.save(sampleUrl);
		
		Url result = repository.findAndCountByShortUrl(SAMPLE_SHORT_URL);
		assertEquals(SAMPLE_REAL_URL, result.getRealUrl());
		
		assertEquals(result.getCallCnt() + 1, repository.findAndCountByShortUrl(SAMPLE_SHORT_URL).getCallCnt());	// 증가된 callCnt
	}
}
