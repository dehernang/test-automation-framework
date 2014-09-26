package example;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * @author hernan
 * @version 1.0
 * @since Aug 28, 2014
 *
 */
public class ExampleJunitClass {

	   private Collection collection;
	   
	    @BeforeClass
	    public static void oneTimeSetUp() {
	        // one-time initialization code   
	    	System.out.println("@BeforeClass - oneTimeSetUp");
	    }
	 
	    @AfterClass
	    public static void oneTimeTearDown() {
	        // one-time cleanup code
	    	System.out.println("@AfterClass - oneTimeTearDown");
	    }
	 
	    @Before
	    public void setUp() {
	        collection = new ArrayList();
	        System.out.println("@Before - setUp");
	    }
	 
	    @After
	    public void tearDown() {
	        collection.clear();
	        System.out.println("@After - tearDown");
	    }
	 
	    @Test
	    public void testEmptyCollection() {
	        assertTrue(collection.isEmpty());
	        System.out.println("@Test - testEmptyCollection");
	    }
	 
	    @Test
	    public void testOneItemCollection() {
	        collection.add("itemA");
	        assertEquals(1, collection.size());
	        System.out.println("@Test - testOneItemCollection");
	    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


