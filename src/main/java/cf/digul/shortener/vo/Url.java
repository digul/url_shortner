package cf.digul.shortener.vo;

import java.sql.Date;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document(collection = "url")
public class Url {

	@Id
	private String id;
	
	@Indexed(unique = true)
	private String realUrl;
	
	@Indexed(unique = true)
	@Size(max = 8, message = "max length is 8 character")
	private String shortUrl;
	
	private long callCnt;
	
	@Transient
	private boolean isNew;
	
	@Transient
	private Date createdTime;	//TODO mongodb 포맷에 맞는 date format 검색해서 생성시점 저장
	
	@Transient
	private String createdIp;	//TODO 요청 ip 찾아내어 생성자 매핑
	

	public Url(String realUrl, String shortUrl) {
		this.realUrl = realUrl;
		this.shortUrl = shortUrl;
		this.callCnt = 0;
		this.isNew = true;
	}
	public Url() {
		
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
	
	public boolean isNew() {
		return isNew;
	}
	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
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
