package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


/**
 * @author hernan
 * @version 1.0
 * @since Aug 22, 2014
 *
 */
public class Config {

	private static final String CONFIG = "config.properties";
	private static final String CONFIG_DEFAULT = "config.properties.default";
	private Properties prop;
	private InputStream input;
	private Map<String, String> properties;
	private static String packagePath;
	
	public Config() {
		prop = new Properties();
		input = null;
		properties = new HashMap<String, String>();
		init();
	}
	
	public void init(){

		try{
			
			Path path = Paths.get(Config.class.getResource(".").toURI());	
			String pathBase = path.getParent().getParent().toString(); //move 4 dirs down			
			input = new FileInputStream(pathBase + "/configs/" + CONFIG);
			prop.load(input);
			Set<Object> keys = prop.keySet();
			for(Object key : keys){
				properties.put(key.toString(), prop.getProperty(key.toString()));
			}
			
		//Get the default	
		}catch(URISyntaxException e){
			getPropertiesDefault();
		}catch(FileNotFoundException e){
			getPropertiesDefault();
		}catch(IOException e){
			getPropertiesDefault();
		}	
 
	}
	
	/**
	 * 
	 * @param key
	 * @return String A property by key
	 */
	public String getProperty(String key){
		return properties.get(key);
	}
	
	/**
	 * 
	 * @return Map All properties
	 */
	public Map<String, String> getProperties(){
		return this.properties;
	}
	
	/**
	 * Default for get properties
	 */
	private void getPropertiesDefault(){	
		System.out.println("config.properties Not Found "+System.getProperty("user.dir")+". Retrieving default.");
		packagePath = Config.class.getName().replace(Config.class.getSimpleName(), "").replace(".", "/");
		String path2 = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath() + packagePath + CONFIG_DEFAULT;
		try{
			input = new FileInputStream(path2);
			prop.load(input);
			Set<Object> keys = prop.keySet();
			for(Object key : keys){
				properties.put(key.toString(), prop.getProperty(key.toString()));
			}
		}catch(IOException e1){
			e1.printStackTrace();
		}	
	}
	
}
