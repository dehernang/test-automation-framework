package example;
/**
 * Simple Oracle database implementation script that establish a connection
 * 
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import util.OracleClient;

/**
 * 
 * @author hernan
 * @version 1.0
 * @since Aug 4, 2014
 *
 */
public class ExampleDBTest {

	public static void main(String[] args) {
		System.out.println ("Begin...");
		String sql = "SELECT COLUMN FROM SCHEMA.TABLE";
		try{
			
			ResultSet result = OracleClient.executeQuery(sql);
			while (result.next()){
				System.out.println (result.getString(1));
			}
			
			OracleClient.close();
		
			sql = "SELECT * FROM SCHEMA.TABLE";
			result = OracleClient.executeQuery(sql);
			while (result.next()){
				System.out.println (result.getString("COLUMN"));
			}
			
			OracleClient.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

}
