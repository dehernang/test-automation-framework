package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import util.Config;

/**
 * 
 * DESCRIPTION:
 * 
 * 		This class extends and wraps the util.TestCaseExt and intended to be used with JUnit. It acts as
 * 		a controller and manager of test cases.
 * 		
 * 
 * DEPENDENCIES:
 * 
 * 		1. Parent class util.TestCaseExt
 * 
 * 		2. Selenium Java Package version 2.42.2
 * 
 * 		3. Selenium Server (Stand Alone) Package version 2.42.2
 * 
 * 		4. JUnit version 4.10
 *  
 *  
 * @author hernan
 * @version 1.0
 * @since Jul 11, 2014
 *
 */
public class TestCaseHelper extends TestCaseExt{

	//protected Map<String, String> element;
	protected ArrayList<Map<String,String>> elementList;

	protected String testing;
	protected int counter;
	protected int counterPass;
	protected int counterFail;
	protected int retval;

	protected static int counterOverall;
	protected static int counterPassOverall;
	protected static int counterFailOverall;

	protected Iterator<String> iterator;
	protected String lnkName;
	protected String msg;

	private String env;
	private String _baseUrl;
	private int _timeout;
	private int _dimensionx;
	private int _dimensiony;
	private int _pointx;
	private int _pointy;
	private Boolean _sharedDriver;
	
	public static final String separator = "------------- ---------------- ---------------";

	public TestCaseHelper() {
		super();
		Config conf = new Config();

		this._baseUrl = null;
		this._timeout = -1;

		env = (String)System.getenv("ENV_TEST_AUTO");
		//println("ENV_TEST_AUTO = " + env + ".");
			
		this._baseUrl = conf.getProperty("baseurl");
		this._timeout = Integer.parseInt(conf.getProperty("timeout").trim());
		this._dimensionx = Integer.parseInt(conf.getProperty("dimensionx").trim());
		this._dimensiony = Integer.parseInt(conf.getProperty("dimensiony").trim());
		this._pointx = Integer.parseInt(conf.getProperty("pointx").trim());
		this._pointy = Integer.parseInt(conf.getProperty("pointy").trim());
		this._sharedDriver = Boolean.valueOf(conf.getProperty("sharedDriver"));

		this.elementList = new ArrayList<Map<String,String>>();
		this.counter = 0;
		this.counterPass = 0;
		this.counterFail = 0;
		this.retval = -1;

	}

	public void finalize(){
		println("Cleaning up...");
		if(super.getDriver() != null)
			super.finalize();
	}
	
	public Boolean init(String testCaseName){

		if(this._baseUrl == null) return false;

		if(this._timeout == -1)
			this._timeout = 30; //default

		//re-use if there is already an instance
		if(_sharedDriver){
			WebDriver dr = super.getDriver();
			if(dr == null){	
				println("Driver is null. Creating new instance...");
				if(!super.setDriver(new FirefoxDriver())) return false;
			}
		}else{
			if(!super.setDriver(new FirefoxDriver())) return false;
		}

		if(!super.setTestCaseName(testCaseName)) return false;

		//setting params based on config file
		try{
			if(!super.setDimension(new Dimension(this._dimensionx,this._dimensiony))) return false;
			if(!super.setPoint(new Point(this._pointx,this._pointy))) return false;
			if(!super.setBaseUrl(this._baseUrl)) return false;	 
			if(!super.setTimeout(this._timeout)) return false;
		}catch(Exception e){ return false; }
		return true;
	}

	public Boolean setDriverBaseUrl(String url){
		if(!super.setBaseUrl(url))
			return false;		
		println("set baseUrl: "+url);
		return true;
	}

	public Boolean setDriverTimeout(int to){
		if(!super.setTimeout(to))
			return false;
		println("set timeout: "+to);
		return true;
	}

	public Boolean setDriverDimension(int x, int y){
		Dimension d = new Dimension(x,y);
		if(!super.setDimension(d))
			return false;
		println("set dimension("+x+","+y+")");
		return true;
	}

	public Boolean setDriverPoint(int x, int y){
		Point p = new Point(1,1);
		if(!super.setPoint(p))
			return false;
		println("set point("+x+","+y+")");
		return true;
	}

	private void generateResult(int result, String method, String type, String target, String locationStr){
		if(result == TestCaseHelper.PASS){
			result(target, super.getTestCaseName(), true, method+"-"+type, locationStr);
			incrementCounterPass();
		}else{
			result(target, super.getTestCaseName(), false, method+"-"+type, locationStr);
			incrementCounterFail();
		}
	}
	
	public String convertToRegEx(String text){
		return "^[\\s\\S]*"+text+"[\\s\\S]*$";
	}

	/*
	 * Printers
	 */

	public void result(String target, String tcName, Boolean pass, String method, String xpath){
		reportTemplate(target,tcName, pass, method, xpath);
	}

	private void reportTemplate(String target, String tcName, Boolean pass, String method, String xpath){
		String status = "Passed";
		if(!pass)
			status = "!Failed!";

		StringBuilder fStr = trimTestCaseName(tcName);

		StringBuilder text = new StringBuilder();
		text.append(status);
		text.append(" <"+fStr+">");
		text.append(" <"+method+">");
		text.append(" <"+target+">");
		if(xpath != null)
			text.append(" <"+xpath+">");
		System.out.println(text);
	}

	//remove the com.xxx
	private StringBuilder trimTestCaseName(String tcName){
		final String dot = ".";
		String[] tmp = tcName.split("\\"+dot);
		StringBuilder str = new StringBuilder();
		String prep = "";
		for(int x=0; x<tmp.length; x++){
			if(x != 0 && x != 1){
				str.append(prep + tmp[x]);
				prep = dot;
			}
		}
		return str;
	}


	public void println(Object msg){
		System.out.println(msg);
	}

	public void printTotalVerification(){
		this.println(super.getTestCaseName() + " TestCase Total: " + this.counter + " Pass: " + this.counterPass + " Fail: " + this.counterFail);
		this.println(super.getTestCaseName() + " Suite Total: " + TestCaseHelper.counterOverall
				+ " Pass: " + TestCaseHelper.counterPassOverall
				+ " Fail: " + TestCaseHelper.counterFailOverall);
		this.println(TestCaseHelper.separator);
	}


	/*
	 * Helpers
	 */

	public Map<String,String> newElement(String k, String v){
		 Map<String,String> tmp = new HashMap<String,String>();
		 tmp.put(k,v);
		 return tmp;
	}

	public void elementListReset(){
		this.elementList = new ArrayList<Map<String,String>>();
	}

	public void wait(int sec) throws InterruptedException{
		long msec = sec * 1000;
		Thread.sleep(msec);
	}

	public void incrementCounterPass(){
		this.counterPass++;
		this.incrementCounters();
		TestCaseHelper.counterPassOverall++;
	}

	public void incrementCounterFail(){
		this.counterFail++;
		this.incrementCounters();
		TestCaseHelper.counterFailOverall++;
	}

	private void incrementCounters(){
		this.counter++;
		TestCaseHelper.counterOverall++;
	}


	/*
	 * Operations
	 */

	public void doVerifyTextPresentList(ArrayList<Map<String, String>> e, String locationStr){
		for(Map<String, String> list: e){
			for(Map.Entry<String, String> listEntry : list.entrySet()){
				
				//@param1 e.g. "xpath", "cssSelector", etc
				//@param2 e.g. "Dodge", "Chevrolet", etc
				//@param3 xpath value e.g. "//div[@id='topic_container']/h1"
				retval = this.doVerifyTextPresent(listEntry.getKey(), listEntry.getValue(), locationStr);
				generateResult(retval,"doVerifyTextPresentList",listEntry.getKey(), listEntry.getValue(), locationStr);
			}
		}
	}

	public void doVerifyElementPresentList(ArrayList<Map<String, String>> e){
		for(Map<String, String> list: e){
			for(Map.Entry<String, String> listEntry : list.entrySet()){		
				
				//@param1 element type e.g. "id", "linkText", etc
				//@param2 e.g. "Dodge", "Chevrolet", etc
				retval = this.doVerifyElementPresent(listEntry.getKey(), listEntry.getValue());
				generateResult(retval,"doVerifyElementPresentList",listEntry.getKey(), listEntry.getValue(), null);
			}
		}
	}

	public void doVerifyTextNotPresentList(ArrayList<Map<String, String>> e, String locationStr){
		for(Map<String, String> list: e){
			for(Map.Entry<String, String> listEntry : list.entrySet()){
				
				//@param1 e.g. "xpath", "cssSelector", etc
				//@param2 e.g. "Dodge", "Chevrolet", etc
				//@param3 xpath value e.g. "//div[@id='topic_container']/h1"
				retval = this.doVerifyTextNotPresent(listEntry.getKey(), listEntry.getValue(), locationStr);
				generateResult(retval,"doVerifyTextNotPresentList",listEntry.getKey(), listEntry.getValue(), locationStr);
			}
		}
	}

	public void doVerifyElementNotPresentList(ArrayList<Map<String, String>> e){
		for(Map<String, String> list: e){
			for(Map.Entry<String, String> listEntry : list.entrySet()){
				
				//@param1 element type e.g. "id", "linkText", etc
				//@param2 e.g. "Dodge", "Chevrolet", etc
				retval = this.doVerifyElementNotPresent(listEntry.getKey(), listEntry.getValue());
				generateResult(retval,"doVerifyElementNotPresentList",listEntry.getKey(), listEntry.getValue(), null);
			}
		}
	}

	public void generateManualResult(int testStatus, String type, String target, String location){
		generateResult(testStatus,"generateManualPassResult", type, target, location);
	}

}
