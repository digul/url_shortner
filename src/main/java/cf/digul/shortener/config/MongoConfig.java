package cf.digul.shortener.config;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	ConnectConfig connectConfig;
	
	@Override
	protected String getDatabaseName() {
		return new MongoClientURI(connectConfig.getUri()).getDatabase();
	}

	@Override
	public MongoClient mongoClient() {
		MongoClientURI mongoClientUri = new MongoClientURI(connectConfig.getUri());
		MongoCredential credential = MongoCredential.createCredential(
				connectConfig.getUserName(), 
				mongoClientUri.getDatabase(), 
				connectConfig.getPassWord().toCharArray()
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
