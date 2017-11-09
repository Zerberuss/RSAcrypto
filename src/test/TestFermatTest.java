/**
 * 
 */
package test;

import static org.junit.Assert.*;
import imsCrypto.ImsInteger;
import imsCrypto.FermatTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author ZugaW
 *
 */
public class TestFermatTest {
	private static long numOfTests = 20000; // defines the number of Fermat Tests executed
	private static boolean outputInfo = false;
	private static int[] testLengths = {25, 50, 100, 200};
	private static int numOfPrimeFactors = 4;
	private static String[] primeSet = {"13", "101", "577", "1297"};
	private static String[] compositeSet = {"14", "105", "579", "1293"};
	private static int multiPrimeRuns = 5;
	private static int multiCompositeRuns = 10000;
	
	private final FermatTest myFermat = new FermatTest(); 
	
	FermatTest getFermat(){
		return myFermat;
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	
	}

	private ImsInteger createRandomPrime(int bitLength){
		
		return new ImsInteger(bitLength);
	}
	
	private ImsInteger createRandomComposite(int bitLength, int primeFactors){
		int primeLength = bitLength/primeFactors;
		ImsInteger composite = ImsInteger.ONE;
		
		for(int i=0; i<primeFactors; i++){
			ImsInteger primeFactor = createRandomPrime(primeLength);
			composite = composite.multiply(primeFactor);
		}
		return composite;
	}

	@Test
	public void testFixedPrimes() {
		for(String prime : primeSet){
			boolean passed = this.getFermat().run( new ImsInteger(prime), numOfTests, outputInfo);
			assertTrue("Your Fermat test failed for prime number " + prime, passed);
		}
	}
	
	@Test
	public void testFixedComposites() {
		for(String composite : compositeSet){
			boolean passed = this.getFermat().run( new ImsInteger(composite), numOfTests, outputInfo);
			assertFalse("Your Fermat test passed for composite number " + composite, passed);
		}
	}
	
	@Test
	public void testPrimeRun() {
		for(int i : testLengths){
			boolean passed = this.getFermat().run(this.createRandomPrime(i), numOfTests, outputInfo);
			assertTrue("Your Fermat test failed for a random prime number of bit length " + i, passed);
		}
	}
	
	@Test
	public void testCompositeRun() {
		for(int i : testLengths){
			boolean passed = this.getFermat().run(this.createRandomComposite(i, numOfPrimeFactors), numOfTests, outputInfo);
			assertFalse("Your Fermat test passed " + numOfTests + " tests for a random composite number of bit length " + i + " having " + numOfPrimeFactors + " prime factors", passed);
		}
	}

	@Test
	public void testMultiPrimeRun() {
		for(int i=0; i<multiPrimeRuns; i++){
			testPrimeRun();
		}
	}
	
	@Test
	public void testMultiCompositeRun() {
		for(int i=0; i<multiCompositeRuns; i++){
			testCompositeRun();
		}
	}
}
