package cf.digul.shortener.document;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlDocument, Long>, UrlRepositoryCustom{
	
	public UrlDocument findByRealUrl(String realUrl);
	public UrlDocument findByShortUrl(String realUrl);
	public UrlDocument findAndCountByShortUrl(String shortUrl);
}
