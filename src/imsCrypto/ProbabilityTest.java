/**
 * 
 */
package imsCrypto;

import java.util.Random;

/**
 * @author ZugaW
 *
 */
public abstract class ProbabilityTest {
	
	// after N tests, the user gets an informational message
	private static long informUserAfterNTests = 10000;

	/**
	 * This function creates a random AseInteger between 0 and modulus
	 * You can use this number as a witness in a Fermat test to check the primality of modulus
	 * 
	 * @param modulus is an upper bound for your test number
	 * @return a random AseInteger guaranteed to be >0 and <modulus
	 */
	protected ImsInteger createRandomTestNumber(ImsInteger modulus){
		
		// get a random number
		ImsInteger testNumber = new ImsInteger(modulus.bitLength(), new Random());
		
		// assure that it is positive and smaller modulus
		testNumber = testNumber.mod(modulus);
		
		// assure that it is non zero
		if(testNumber.compareTo(ImsInteger.ZERO)==0){
			testNumber = modulus.subtract(ImsInteger.ONE);
		}
		return testNumber;
	}
	
	/**
	 * @param possiblePrime holds the number to check for primality
	 * @param testNumber is the test number used for the test execution
	 * @return true if the test was passed, false if the test failed
	 */
	abstract boolean runOnce(ImsInteger possiblePrime, ImsInteger testNumber);
	
	/**
	 * @param possiblePrime is the number to check for primality
	 * @param numOfTests is the number of times you want the test to run
	 * @param outputInfo decides whether info should be output to stdout for the user
	 * @return gives true if possiblePrime passed all tests, else false
	 */
	public final boolean run(ImsInteger possiblePrime, final long numOfTests, final boolean outputInfo){
		if(numOfTests <=0){
			return false;
		}
							
		StopWatch watch = new StopWatch();
		watch.start();
		
		////////////////////////////////////////////////////////////////////
		// start a series of numOfTests FermatTests
		// that is exponentiate a test number 
		long counter = 1;
		for(counter=1; counter<=numOfTests; counter++){
			
			// create a random AseInteger as witness
			ImsInteger testNumber = this.createRandomTestNumber(possiblePrime);
			
			final boolean success = runOnce(possiblePrime, testNumber);
			
			if( !success){
				// test failed
				watch.stop();
				if(outputInfo){
					System.out.println("Test " + counter + " failed after " + watch.getElapsedTime() + " ms, " + possiblePrime + " is not a prime number!");
				}
				return false;
			}
			
			// inform the user of current state
			if(outputInfo && counter%informUserAfterNTests==0)
			{
				System.out.println(counter + " tests succeeded after " + watch.getElapsedTime() + " ms");
			}
		}
		
		// adjust counter to the number of tests actually done for correct output
		counter--;
		
		// all Fermat tests succeeded
		watch.stop();
		if(outputInfo){
			System.out.println("\nAfter " + watch.getElapsedTime() + " ms " + possiblePrime + " finally passed " + counter + " tests!" );
		}
		return true;
	}
}
