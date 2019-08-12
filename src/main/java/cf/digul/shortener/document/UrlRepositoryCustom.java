package cf.digul.shortener.document;

public interface UrlRepositoryCustom {

	public UrlDocument findAndCountByShortUrl(String shortUrl);
}
