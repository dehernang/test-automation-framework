package util;

import java.net.UnknownHostException;
import com.mongodb.MongoClient;
import util.Config;

/**
 * @author hernan
 * @version 1.0
 * @since Sep 4, 2014
 *
 */
public class MongoClientExt {

	private static MongoClientExt mongo = null;
	private MongoClient mongoClient = null;
	
	protected MongoClientExt(){
		Config conf = new Config();
		try{
			mongoClient = new MongoClient(
					conf.getProperty("mongo_host"), 
					Integer.parseInt(conf.getProperty("mongo_port"))
				);		
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
	}
	
	public static MongoClientExt getInstance() {
		if(mongo == null) {
			mongo = new MongoClientExt();
		}
	    return mongo;
	    
	}

	/**
	 * @return the mongoClient
	 */
	public MongoClient getMongoClient() {
		return this.mongoClient;
	}

	/**
	 * @param mongoClient the mongoClient to set
	 */
	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

}
