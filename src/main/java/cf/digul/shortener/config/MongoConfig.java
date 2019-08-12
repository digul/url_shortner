package cf.digul.shortener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	@Value("${spring.data.mongodb.username}")
	private String userName;
	@Value("${spring.data.mongodb.password}")
	private String passWord;
	@Value("${spring.data.mongodb.uri}")
	private String uri;
	
	@Override
	protected String getDatabaseName() {
		return new MongoClientURI(this.uri).getDatabase();
	}

	@Override
	public MongoClient mongoClient() {
		MongoClientURI mongoClientUri = new MongoClientURI(this.uri);
		MongoCredential credential = MongoCredential.createCredential(userName, mongoClientUri.getDatabase(), passWord.toCharArray());
		ServerAddress serverAddress = new ServerAddress(mongoClientUri.getHosts().get(0));
		MongoClientOptions options = MongoClientOptions.builder().build();
		return new MongoClient(serverAddress, credential, options);
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoClient(), getDatabaseName());
	}
}
