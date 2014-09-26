package util;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 
 * Description: 
 *  	API client class that encapsulates Apache http request and response.
 *  
 *  Dependencies:
 *  	1. httpclient-4.3.5.jar
 *  	2. httpclient-cache-4.3.5.jar
 *  	3. httpcore-4.3.2.jar
 *  	4. httpmime-4.3.5.jar
 *  	5. fluent-hc-4.3.5.jar
 *  	6. commons-codec-1.6.jar
 *  	7. commons-logging-1.1.3.jar 
 * 
 * @author hernan
 * @version 1.0
 * @since Aug 14, 2014
 *
 */
abstract public class ApiClient {

	
	private HttpRequest httpRequest;
	private String url;
	private int method;
	private Map<String, String> header;
	private HttpEntity entity;
	private HttpResponse response;
	
	protected HttpClient httpclient;
	
	public static final int GET = 1;
	public static final int POST = 2;
	public static final int PUT = 3;
	public static final int DELETE = 4;
	public static final String UNICODE = "UTF-8";
	public static final String AUTH = "Authorization";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String MIME_JSON = "application/json";
	
	/**
	 * 
	 * @param type
	 * @param url
	 * @throws Exception
	 */
	public ApiClient(int type, String url) throws Exception{
		
		this.httpclient = HttpClients.createDefault();
		this.url = url;
		this.header = new HashMap<String, String>();
		this.response = null;
		switch(type){
			case GET:
				this.setMethod(GET);
				break;
			case POST:
				this.setMethod(POST);
				break;
			case PUT:
				this.setMethod(PUT);
				break;
			case DELETE:
				this.setMethod(DELETE);
				break;
			default:
				throw new Exception();
		}
	}
	
	
	/**
	 * @return httpclient HttpClient
	 */
	protected HttpClient getHttpclient() {
		return this.httpclient;
	}

	/**
	 * @return method int
	 */
	protected int getMethod() {
		return this.method;
	}
	
	/**
	 * @return header Map
	 */
	protected Map<String, String> getHeader() {
		return this.header;
	}
	
	/**
	 * 
	 * @return entity HttpEntity 
	 */
	protected HttpEntity getEntity(){
		return this.entity;
	}
	
	/**
	 * 
	 * @return response HttpResponse
	 */
	protected HttpResponse getResponse(){
		return this.response;
	}
	
	/**
	 * 
	 * @return String URL
	 */
	protected String getUrl(){
		return this.url;
	}
	
	/**
	 * @return the httpRequest
	 */
	public HttpRequest getHttpRequest() {
		return httpRequest;
	}
	
	/**
	 * 
	 * @param method int 1=GET, 2=POST, 3=PUT, 4=DELETE
	 */
	protected void setMethod(int method) throws Exception{
		
		switch(method){
			case GET:
				this.method = GET;		
				this.httpRequest = new HttpGet(this.url);
				break;
			case POST:
				this.method = POST;		
				this.httpRequest = new HttpPost(this.url);
				break;
			case PUT:
				this.method = PUT;		
				this.httpRequest = new HttpPut(this.url);
				break;
			case DELETE:
				this.method = DELETE;		
				this.httpRequest = new HttpDelete(this.url);
				break;
			default:
				throw new Exception();
		}
	}
	
	/**
	 * 
	 * @param entity HttpEntity JSON format
	 */
	protected void setEntity(HttpEntity entity){
		this.entity = entity;
	}
	
	/**
	 * Override url field
	 * @param url String URL
	 */
	protected void setUrl(String url){
		this.url = url;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	
	protected void clean(){
		this.header = null;
		this.entity = null;
	}
	
	/**
	 * 
	 * @return Map<String, String>
	 */
	public abstract Map<String, String> sendRequest();
	
	/**
	 * 
	 * @param json String JSON format
	 * @param field String
	 * @return
	 */
	public static String getJSONValue(String json, String field){
		JSONObject jsonObj;
		try{
			jsonObj = new JSONObject(json);			
		}catch(NullPointerException e){
			return null;
		}catch(JSONException e){
			return null;
		}
		return jsonObj.optString(field);
	}

	/**
	 * 
	 * @param json
	 * @param item
	 * @return
	 */
	public static String getJSONValues(String json, String child){
		JSONObject childNodeObj;
		try{
			JSONObject mainNodeObj = new JSONObject(json);
			childNodeObj = (JSONObject) mainNodeObj.get(child);			
		}catch(NullPointerException e){
			return null;
		}catch(JSONException e){
			return null;
		}
		return childNodeObj.toString();
	}
	

	

}


