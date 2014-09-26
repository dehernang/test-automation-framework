package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.ApiClient;
import util.Config;


/**
 * @author hernan
 * @version 1.0
 * @since Aug 22, 2014
 *
 */
public class ApigeeApiClient extends ApiClient {

	private List<NameValuePair> params;
	
	/**
	 * @throws Exception 
	 * 
	 */
	public ApigeeApiClient(int type, String url) throws Exception {
		super(type, url);
		Config conf = new Config();
		this.params = new ArrayList<NameValuePair>();
		this.params.add(new BasicNameValuePair("client_id", conf.getProperty("apigee_client_id")));
		this.params.add(new BasicNameValuePair("client_secret", conf.getProperty("apigee_client_secret")));
		this.params.add(new BasicNameValuePair("grant_type", conf.getProperty("apigee_grant_type")));		
	}

	/**
	 * @return the params
	 */
	public List<NameValuePair> getParams() {
		return params;
	}
	
	/**
	 * 
	 * @param entity String JSON format
	 */
	public void autoAddTokenParams(){
		System.out.println(this.params);
		try {
			super.setEntity(new UrlEncodedFormEntity(this.params, UNICODE));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param response HttpResponse
	 * @return String 
	 */
	public static String readResponse(HttpResponse response){
		StringBuilder builder = new StringBuilder();
		try{
			
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), UNICODE));
	    	for (String line = null; (line = reader.readLine()) != null;) {
	    	    builder.append(line).append("\n");
	    	}	
	    		
		}catch(NullPointerException e){
			return null;
		}catch(Exception e){
	        e.printStackTrace();
	    }   	
		return builder.toString();
	}
	
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
	 * Get JSON Array from String format
	 * 
	 * @param json String
	 * @param field String
	 * @return JSONArray
	 */
	public static JSONArray getJSONArray(String json, String field){
		try{
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getJSONArray(field);
		}catch(JSONException e){ return null; }
		
	}
	
	/**
	 * 
	 * Convert JSONArray into ArrayList
	 * 
	 * @param jsonArr JSONArray
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> convertJSONArrToArrList(JSONArray jsonArr){
		ArrayList<String> list = new ArrayList<String>();     
		JSONArray jsonArray = jsonArr; 
		if(jsonArray != null){ 
			int len = jsonArray.length();
			for (int i=0;i<len;i++){ 
				list.add(jsonArray.get(i).toString());
			} 
		} 
		return list;
	}
	

	
	/**
	 * 
	 * Search JSONArray by id
	 * 
	 * @param jArr JSONArray
	 * @param field String
	 * @param value String
	 * @return String 
	 */
	public static String searchJSONArray(JSONArray jArr, String key, String value){
		if(jArr instanceof JSONArray){
			for(int i=0; i<jArr.length(); i++){
				JSONObject mainNodeObj = jArr.getJSONObject(i);	
				if(mainNodeObj.get(key).equals(value)){			
					return jArr.get(i).toString();
				}
			}
		}
		return null;
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
	
	/**
	 * 
	 * @param jsonStr
	 * @param idName
	 * @param idValue
	 * @return
	 */
	public static String getJSONResult(String jsonStr, String param, String idName, String idValue){
		
		if(jsonStr == null || param == null){ return null; }
		
		String json = null;
		
		if(idName == null || idValue == null){ 
			//return JSON entry or array
			return getJSON(jsonStr, param);	
		}
	
		//get the specific entry from the JSON array
		JSONArray resultArr = ApigeeApiClient.getJSONArray(jsonStr, param);
				
		if(resultArr != null){
			json = ApigeeApiClient.searchJSONArray(resultArr, idName, idValue);		
		}else{		
			String jsonTmp1 = ApigeeApiClient.getJSONValues(jsonStr, param);		
			JSONObject jsonTmp2 = new JSONObject(jsonTmp1);		
			if(idValue.equals(jsonTmp2.get(idName).toString())){		
				json = jsonTmp1;
			}else{
				json = null;
			}					
		}
		
		return json;
		
	}
	
	public static String getJSON(String jsonStr, String param){
		JSONObject jsonObj = new JSONObject(jsonStr);
		return jsonObj.optString(param);
	}
	
	/**
	 * 
	 * @param key String
	 * @param value String
	 */
	public void addHeader(String key, String value){
		if(key.equals(ApiClient.AUTH)){
			super.getHeader().put(ApiClient.AUTH, "Bearer " + value);
		}else{
			super.getHeader().put(key, value);
		}
	}
	
	/**
	 * 
	 * @return String
	 */
	public Map<String, String> sendRequest(){
		HttpResponse res = request();
		Map<String, String> response = new HashMap<String, String>();
		response.put("code", res.getStatusLine().toString());
		response.put("result", readResponse(res));
		return response;
	}
	
	/**
	 * 
	 * @param entity String JSON format
	 */
	public void setData(String entity){
		try {
			super.setEntity(new StringEntity(entity));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @return HttpResponse
	 */
	private HttpResponse request(){	

		try{
			switch(getMethod()){
			
				case GET:

					for(Map.Entry<String, String> entry : getHeader().entrySet()){
						((HttpGet)getHttpRequest()).setHeader(entry.getKey(), entry.getValue());
					}
					
					break;

				case POST:

					for(Map.Entry<String, String> entry : getHeader().entrySet()){
						((HttpPost)getHttpRequest()).setHeader(entry.getKey(), entry.getValue());
					}
	
					if(getEntity() != null){
						((HttpPost)getHttpRequest()).setEntity(getEntity());
					}
	
					break;
				
				case PUT:
					
					for(Map.Entry<String, String> entry : getHeader().entrySet()){
						((HttpPut)getHttpRequest()).setHeader(entry.getKey(), entry.getValue());
					}
	
					if(getEntity() != null){
						((HttpPut)getHttpRequest()).setEntity(getEntity());
					}
					
					break;
					
				case DELETE:
					
					for(Map.Entry<String, String> entry : getHeader().entrySet()){
						((HttpDelete)getHttpRequest()).setHeader(entry.getKey(), entry.getValue());
					}
					
					break;
					
				default:
					throw new Exception();
					
			}	
			
			setResponse(null);
			setResponse(httpclient.execute((HttpUriRequest) getHttpRequest()));
			
		}catch(Exception e){
			this.clean();
			e.printStackTrace();
		}
		
		clean();
		return getResponse();
	}
	
	/**
	 * 
	 * @return String random size of 10 alpha-numeric characters
	 */
	public static String generateRandomStr(){
		Random r = new Random();
		String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			builder.append(alphabet.charAt(r.nextInt(alphabet.length())));
		} 
		return builder.toString();
	}

	/**
	 * Short method to print
	 * @param obj
	 */
	public static void print(Object obj){
		System.out.println(obj);
	}
	
	/**
	 * 
	 * @param url
	 * @param id
	 * @param secret
	 * @return
	 */
	public static String getToken(String url, String id, String secret){
		
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url + "/token");
		HttpResponse response = null;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", id));
		params.add(new BasicNameValuePair("client_secret", secret));
		params.add(new BasicNameValuePair("grant_type", "client_credentials"));

		try{
			httpPost.setEntity(new UrlEncodedFormEntity(params, UNICODE));
			response = httpclient.execute(httpPost);
		}catch(UnsupportedEncodingException e){ return null;
		}catch(IOException e){ return null; }
		
		return getJSONValue(readResponse(response), "access_token");
		
	}
	
	/**
	 * 
	 * @param server deleteSolrDataById
	 * @param field String
	 * @param id String
	 * @param verify boolean
	 * @return
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static boolean deleteSolrDataById(HttpSolrServer server, String field, String id, boolean verify) throws SolrServerException, IOException{
		
		//Delete test data			
		server.deleteByQuery(field + ":" + id);
		server.commit();

		if(verify){	
			//Verify data deleted in Solr				
			SolrDocumentList docList = getSolrData(server, field, id);
			//ApiClient.print(docList);
			//Negate since we verify that data doesn't exists anymore after deletion
			return !verifyDataExists(docList, field, id);
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param solr HttpSolrServer
	 * @param params Map<String, String>
	 * @return
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static boolean createSolrData(HttpSolrServer solr, Map<String, String> params) throws SolrServerException, IOException{
		
		SolrInputDocument doc = new SolrInputDocument();		
		for(Map.Entry<String, String> entry : params.entrySet()){
			doc.addField(entry.getKey(), entry.getValue());
		}
		
		UpdateResponse result = solr.add(doc);	    
		solr.commit(); 
		//verify created
		String resultStr = result.toString();
		if(!resultStr.contains("status=0")){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param server HttpSolrServer
	 * @param field String
	 * @param id String
	 * @return SolrDocumentList
	 * @throws SolrServerException
	 */
	public static SolrDocumentList getSolrData(HttpSolrServer server, String field, String id) throws SolrServerException{
		SolrQuery query = new SolrQuery();
		query.setQuery(field + ":" + id);
		QueryResponse qr = server.query(query);		
		return qr.getResults();	
	}
	
	/**
	 * 
	 * @param docList SolrDocumentList
	 * @param field String
	 * @param id String
	 * @return
	 */
	public static boolean verifyDataExists(SolrDocumentList docList, String field, String id){
		boolean exists = false;
		//Search through the doc to verify it exists
		for (SolrDocument solrDoc : docList) {
		    String idTmp = (String)solrDoc.get(field);
		    if(idTmp.equals(id)){
		    	exists = true;
		    }
		}
		return exists;
	}
	
	/**
	 * 
	 * @param docList SolrDocumentList
	 * @return
	 */
	public static Map<String,String> convertSolrDocToMap(SolrDocumentList docList){
		Map<String,String> list = new HashMap<String,String>();
		for (SolrDocument solrDoc : docList) {	
			for(Map.Entry<String, Object> entry : solrDoc.entrySet()){
				list.put(entry.getKey(), entry.getValue().toString());
			}			
		}
		return list;
	}
	
}
