package cf.digul.shortener.vo;

import javax.validation.constraints.Size;

public class Url {

	private String realUrl;
	@Size(max = 8, message = "shortUrl cannot over 8 characters.")
	private String shortUrl;
	private int callCnt;
	
	public Url(String realUrl, String shortUrl) {
		super();
		this.realUrl = realUrl;
		this.shortUrl = shortUrl;
		this.callCnt = 0;
	}
	
	public String getRealUrl() {
		return realUrl;
	}
	public String getShortUrl() {
		return shortUrl;
	}
	public int getCallCnt() {
		return callCnt;
	}
}
