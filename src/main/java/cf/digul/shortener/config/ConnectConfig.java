package cf.digul.shortener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource(value = "file:D:/workspace/url_shortner/src/main/resources/config.properties", ignoreResourceNotFound = true),
	@PropertySource(value = "file:/deploy/shortener/config.properties", ignoreResourceNotFound = true)
})
public class ConnectConfig {
	
	@Value("${spring.data.mongodb.username}")
	private String userName;
	@Value("${spring.data.mongodb.password}")
	private String passWord;
	@Value("${spring.data.mongodb.uri}")
	private String uri;
	@Value("${spring.data.mongodb.authentication-database}")
	private String adminUser;
	
	
	public String getUserName() {
		return userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public String getUri() {
		return uri;
	}
	public String getAdminUser() {
		return adminUser;
	}
	
}
