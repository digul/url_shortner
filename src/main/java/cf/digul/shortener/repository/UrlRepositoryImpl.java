package cf.digul.shortener.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import cf.digul.shortener.vo.Url;

public class UrlRepositoryImpl implements UrlRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public UrlRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	@Override
	public Url findAndCountByShortUrl(String shortUrl) {
		Query query = new Query();
		query.addCriteria(Criteria.where("shortUrl").is(shortUrl));
		
		Update update = new Update();
		update.inc("callCnt", 1);
		
		FindAndModifyOptions option = new FindAndModifyOptions();
		option.returnNew(true);
		
		Url resultUrl = mongoTemplate.findAndModify(query, update, option, Url.class); 
		return resultUrl;
	}
	@Override
	public Url findOneByShortUrl(String shortUrl) {
		Query query = new Query();
		query.addCriteria(Criteria.where("shortUrl").is(shortUrl));
		return mongoTemplate.findOne(query, Url.class);
	}
	@Override
	public Url findOneByRealUrl(String realUrl) {
		Query query = new Query();
		query.addCriteria(Criteria.where("realUrl").is(realUrl));
		return mongoTemplate.findOne(query, Url.class);
	}

}
