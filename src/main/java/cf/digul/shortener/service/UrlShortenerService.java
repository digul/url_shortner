package cf.digul.shortener.service;


import org.springframework.stereotype.Service;

import cf.digul.shortener.vo.Url;

@Service
public class UrlShortenerService {
	
	public Url generateShortUrl(String realUrl) {
		return new Url(realUrl, "aAbBcCdD");
	}
	
	public Url findUrl(String shortUrl) {
		return new Url("test.real.url", shortUrl);
	}
}
