package example;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import util.Config;

/**
 * @author hernan
 * @version 1.0
 * @since Sep 12, 2014
 *
 */
public class ExampleTestConfig {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws URISyntaxException {
		
		Path path = Paths.get(ExampleTestConfig.class.getResource(".").toURI());
		System.out.println(path.getParent());               // <-- Parent directory
		System.out.println(path.getParent().getParent()); 
		System.out.println(path.getParent().getParent().getParent()); 
		
		
		Config config = new Config();
		Map<String, String> conf = config.getProperties();		    	
		String var = conf.get("neb_url");
		System.out.println(var);

	}

}
