package example;

import java.net.UnknownHostException;
import java.util.Date;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author hernan
 * @version 1.0
 * @since Sep 4, 2014
 *
 */
public class ExampleMongoClient {

	public static void main(String[] args) {
		try {
			
			String ans = "aaabc";
			
			MongoClient mongo = new MongoClient( "localhost" , 27017 );
			System.out.println(mongo);

			/* 
			//get all dbs
			List<String> dbs = mongo.getDatabaseNames();
			for(String db : dbs){
				System.out.println(db);
			}
			*/
			
			DB db = mongo.getDB("sampledb");
			DBCollection table = db.getCollection("captchas");
			System.out.println(table);
			
			
			BasicDBObject document = new BasicDBObject();
			document.put("answer", ans);
			document.put("createdDate", new Date());
			table.insert(document);
			
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("answer", ans);
			
			/*
			DBCursor cursor = table.find(searchQuery);
			DBObject o = null;
			while (cursor.hasNext()) {
				o = cursor.next();
				System.out.println(o);
				String id = o.toMap().get("_id").toString();
				System.out.println(id);
			}
			*/
			
			 
			DBCursor cursor = table.find(searchQuery);
			DBObject o = cursor.next();
			String id = o.toMap().get("_id").toString();
			System.out.println(id);
					
					
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
