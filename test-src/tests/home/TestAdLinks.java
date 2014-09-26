package tests.home;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.TestCaseHelper;

/**
 * 
 * @author hernan
 * @version 1.0
 * @since Jul 7, 2014
 *
 */
public class TestAdLinks extends TestCaseHelper{
  
	@BeforeClass
	public void oneTimeSetUp() throws Exception {
		Boolean good = super.init(TestAdLinks.class.getName());
		if(!good)
			throw new Exception();
	}

	@Test(priority=1)
	public void testHomeAdLinks() throws Exception {

		click(CSS_SELECTOR,"img[alt=\"Go To ProjectX Home Page\"]");

	    elementList.add(newElement(LINK_TEXT, "A Link"));
	    elementList.add(newElement(LINK_TEXT, "Another Link"));
	    doVerifyElementPresentList(elementList);

	   
	  }
	
	@AfterClass
	  public void oneTimeTearDown() throws Exception {
		  printTotalVerification();
		  String verificationErrorString = this.getVerificationErrors().toString();
		  if (!"".equals(verificationErrorString)) {
			  fail(verificationErrorString);
		  }
	  }
	
	  
	
}
