package cf.digul.shortener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClient;

@Configuration
@PropertySources({
	@PropertySource(value = "file:D:/workspace/url_shortner/src/main/resources/mongoConfig.properties"),
})
@Profile(value = {"default"})
public class MongoConfigLocal extends AbstractMongoConfiguration {

	@Value("${spring.data.mongodb.username}")
	private String userName;
	@Value("${spring.data.mongodb.password}")
	private String passWord;
	@Value("${spring.data.mongodb.uri}")
	private String uri;
	@Value("${spring.data.mongodb.authentication-database}")
	private String adminUser;
	
	@Override
	protected String getDatabaseName() {
		return new MongoClientURI(this.uri).getDatabase();
	}

	@Override
	public MongoClient mongoClient() {
		MongoClientURI mongoClientUri = new MongoClientURI(this.uri);
		MongoCredential credential = MongoCredential.createCredential(
				this.userName, 
				mongoClientUri.getDatabase(), 
				this.passWord.toCharArray()
			);
		ServerAddress serverAddress = new ServerAddress(mongoClientUri.getHosts().get(0));
		MongoClientOptions options = MongoClientOptions.builder().build();
		return new MongoClient(serverAddress, credential, options);
	}
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoClient(), getDatabaseName());
	}
}
