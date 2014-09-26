package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import util.Config;


/**
 * 
 * DESCRIPTION:
 * 
 * 		Oracle database connector using JDBC driver version 6 for Oracle
 * 		Database 11g Release 2
 * 
 * DEPENDENCIES:
 * 
 * 		1. JDBC Driver - ojdbc6.jar
 * 
 * 
 * @author hernan
 * @version 1.0
 * @since Aug 4, 2014
 *
 */
public class OracleClient {

	private static String _hostname;
	private static int _port;
	private static String _sid;
	private static String _username;
	private static String _password;
	private static String _dbenv;
	private static Connection _instance;
	private static boolean _debug;
	private static Statement _stmt;
	private static ResultSet _rset;
	
	/**
	 * Reset instance
	 * @return
	 */
	public static Connection getNewInstance(){
		_instance = null;
		Config conf = new Config();
		String tmp = conf.getProperty("debug");
		if(tmp.equals("true")){ _debug = true; }else{ _debug = false; }
		try{
			if(_debug){ println("creating new jdbc connection instance..."); }
			_hostname = conf.getProperty("hostname");
			_port = Integer.parseInt(conf.getProperty("port").trim());
			_sid = conf.getProperty("sid");
			_username = conf.getProperty("username");
			_password = conf.getProperty("password");
			_dbenv = conf.getProperty("dbenv");
			_instance = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+_hostname+":"+_port+":"+_sid, 
					_username, 
					_password
				);
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return _instance;
	}
	
	/**
	 * 
	 * @return Connection instance
	 */
	public static Connection getInstance(){
		Config conf = new Config();
		String tmp = conf.getProperty("debug");
		if(tmp.equals("true")){ _debug = true; }else{ _debug = false; }
		if(_instance == null){
			try{
				if(_debug){ println("creating new jdbc connection instance..."); }
				_hostname = conf.getProperty("hostname");
				_port = Integer.parseInt(conf.getProperty("port").trim());
				_sid = conf.getProperty("sid");
				_username = conf.getProperty("username");
				_password = conf.getProperty("password");
				_dbenv = conf.getProperty("dbenv");
				_instance = DriverManager.getConnection(
						"jdbc:oracle:thin:@"+_hostname+":"+_port+":"+_sid, 
						_username, 
						_password
					);
			}catch(SQLException e){ 
				e.printStackTrace();
				return null;
			}
		}else{
			if(_debug){ println("reusing jdbc connection instance..."); }
		}	
		return _instance;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDbEnvironment(){
		return _dbenv;
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	public static void commit() throws SQLException{
		getInstance().commit();
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException{
		getInstance().close();
		_instance = null;
	}
	
	/**
	 * 
	 * @throws SQLException
	 */	
	public static void closeStatement() throws SQLException{
		_stmt.close();
	}
	
	/**
	 * 
	 * @throws SQLException
	 */	
	public static void close() throws SQLException{
		commit();
		closeStatement();
		closeConnection();
	}
	
	/**
	 * 
	 * @param msg
	 */
	private static void println(Object msg){
		System.out.println(msg);
	}
	
	/**
	 * 
	 * @param sql UPDATE, INSERT, DELETE SQL Query
	 * @return boolean
	 * @throws SQLException
	 */
	public static boolean executeUpdate(String sql) throws SQLException{
		
		if(getInstance().isClosed()){
			println("renewing connection");
			_stmt = getNewInstance().createStatement();
		}else{
			_stmt = getInstance().createStatement();	
		}
		
		if(_stmt.executeUpdate(sql) == 1){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @param sql SELECT SQL Query
	 * @return ResultSet
	 * @throws SQLException
	 */
	public static ResultSet executeQuery(String sql) throws SQLException{
		
		if(getInstance().isClosed()){
			println("renewing connection");
			_stmt = getNewInstance().createStatement();
		}else{
			_stmt = getInstance().createStatement();	
		}

		_rset = _stmt.executeQuery (sql);
		return _rset;
		
	}
	
	
	
}
