package cf.digul.shortener.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cf.digul.shortener.vo.Url;
import cf.digul.shortener.repository.UrlRepository;
import cf.digul.shortener.util.ShortUrlGenerator;

@Service
public class UrlShortenerService {
	private static final Logger logger = LogManager.getLogger(UrlShortenerService.class);
	
	@Autowired
	private UrlRepository urlRepository;
	
	/**
	 * URL 매핑정보 저장
	 * @param realUrl
	 * @return Url which have generated shortUrl
	 */
	@Transactional(rollbackFor = Exception.class)
	public Url saveUrl(String realUrl) {
		try {
			Url searchedUrl = urlRepository.findOneByRealUrl(realUrl);
			if(searchedUrl != null) {
				searchedUrl.setIsNew(false);
				return searchedUrl;
			} 
			
			Url savedUrl = urlRepository.save(new Url(realUrl));
			savedUrl.setShortUrl(ShortUrlGenerator.encode(savedUrl.getId()));
			savedUrl = urlRepository.save(savedUrl);
			savedUrl.setIsNew(true);
			return savedUrl;
			
		} catch(Exception e) {
			// mongodb는 transaction지원하지 않아서 직접 delete 호출함
			logger.error("## UrlShortenerService ## saveUrl error. rollback..");
			urlRepository.deleteByRealUrl(realUrl);
			throw e;
		}
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
