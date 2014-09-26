package example;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class ExampleTestNG {
	
	@Test(priority=2)
	public void verifySecond() {
		System.out.println("second");
	}
	  
	@Test(priority=1)
	public void verifyFirst() {
		System.out.println("first");
	}
	  
	@BeforeClass
	public void beforeClass() {
	}
	
	@AfterClass
	public void afterClass() {
	}

}
