package cf.digul.shortener.config;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import com.mongodb.MongoClient;

@Configuration
@Profile(value = {"default", "dev"})
public class MongoConfigLocal extends AbstractMongoConfiguration {
	private static final Logger logger = LogManager.getLogger(MongoConfigLocal.class);
	private String host = "127.0.0.1";
	private int port = 27017;
	private String database = "shortening";
	
	@Override
	protected String getDatabaseName() {
		return this.database;
	}

	@Override
	public MongoClient mongoClient() {
		
		startEmbeddedMongo();
		
		ServerAddress serverAddress = new ServerAddress(host);
		MongoClientOptions options = MongoClientOptions.builder().build();
		return new MongoClient(serverAddress, options);
	}
	
	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), getDatabaseName());
	}
	
	private void startEmbeddedMongo() {
		logger.info("## start to embedded mongodb ##");
		
		MongodExecutable mongodExecutable;
		try {
			IMongodConfig mongodConfig = new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION)
					.net(new Net(this.host, this.port, Network.localhostIsIPv6()))
					.build();
			
			mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
			logger.info("## MongodStarter prepared ##");
			
			mongodExecutable.start();
			logger.info("## MongodProcess stated ##");
			
		} catch(IOException e) { 
			logger.error("## embedded mongodb has FAILED to start ##");
		}
		logger.info(String.format("## started embedded mongodb successfully ## %s:%d/%s", this.host, this.port, this.database));
	}
	
}
