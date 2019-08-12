package cf.digul.shortener.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UrlRepositoryImpl implements UrlRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public UrlRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	@Override
	public UrlDocument findAndCountByShortUrl(String shortUrl) {
		Query query = new Query();
		query.addCriteria(Criteria.where("shortUrl").is(shortUrl));
		
		Update update = new Update();
		update.inc("callCnt", 1);
		
		FindAndModifyOptions option = new FindAndModifyOptions();
		option.returnNew(true);
		
		UrlDocument resultUrl = mongoTemplate.findAndModify(query, update, option, UrlDocument.class); 
		return resultUrl;
	}

}
