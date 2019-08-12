package cf.digul.shortener.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document(collection = "url")
public class UrlDocument {

	@Id
	private String id;
	
	@Indexed(unique = true)
	private String realUrl;
	
	@Indexed(unique = true)
	private String shortUrl;
	
	private long callCnt;
	
	
	public UrlDocument() {
		this.callCnt = 0;
	}
	
	
	public String getId() {
		return id;
	}

	public String getRealUrl() {
		return realUrl;
	}
	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}
	
	public String getShortUrl() {
		return shortUrl;
	}
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}
	
	public long getCallCnt() {
		return callCnt;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}
}
