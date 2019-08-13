package cf.digul.shortener.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import cf.digul.shortener.vo.Url;

public interface UrlRepository extends MongoRepository<Url, Long>, UrlRepositoryCustom{
	
	public List<Url> findByRealUrl(String realUrl);
	public List<Url> findByShortUrl(String shortUrl);
	
	@Query(value = "{realUrl : ?0}", delete = true)
	public void deleteByRealUrl(String realUrl);
}
