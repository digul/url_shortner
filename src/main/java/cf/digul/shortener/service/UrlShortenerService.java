package cf.digul.shortener.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cf.digul.shortener.vo.Url;
import cf.digul.shortener.repository.UrlRepository;

@Service
public class UrlShortenerService {
	
	@Autowired
	private UrlRepository urlRepository;
	
	/**
	 * URL 매핑정보 저장
	 * @param realUrl
	 * @return Url which have generated shortUrl
	 */
	public Url saveUrl(String realUrl) {
		Url searchedUrl = urlRepository.findOneByRealUrl(realUrl);
		if(searchedUrl != null) {
			searchedUrl.setIsNew(false);
			return searchedUrl;
		} 
		
		String shortUrl;
		do {
			shortUrl = generateShortUrl(realUrl);
		} while(urlRepository.findByShortUrl(shortUrl) == null); 
		
		Url newUrl = new Url(realUrl, shortUrl);
		
		return urlRepository.save(newUrl);
	}

	/**
	 * short url generate
	 * @param realUrl
	 * @return
	 */
	private String generateShortUrl(String realUrl) {
		//TODO short url 만드는 로직 구현
		return "GenURL2";
	}
	
	/**
	 * 매핑된 URL 탐색
	 * @param shortUrl
	 * @return Url which is found from DB
	 */
	public Url findUrl(String shortUrl) {
		return urlRepository.findAndCountByShortUrl(shortUrl);
	}
	
}
