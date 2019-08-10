package cf.digul.shortener.vo;

public class Url {

	private String realUrl;
	private String shortUrl;
	
	public Url(String realUrl) {
		super();
		this.realUrl = realUrl;
		this.shortUrl = "sample";
	}
	
	public String getRealUrl() {
		return realUrl;
	}
	public String getShortUrl() {
		return shortUrl;
	}
}
