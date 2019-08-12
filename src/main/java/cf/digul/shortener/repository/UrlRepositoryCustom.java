package cf.digul.shortener.repository;

import cf.digul.shortener.vo.Url;

public interface UrlRepositoryCustom {

	public Url findAndCountByShortUrl(String shortUrl);
	public Url findOneByShortUrl(String shortUrl);
	public Url findOneByRealUrl(String realUrl);
}
