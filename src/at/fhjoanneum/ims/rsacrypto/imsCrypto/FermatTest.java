package at.fhjoanneum.ims.rsacrypto.imsCrypto;

public class FermatTest extends ProbabilityTest {
	
	/**
	 * @param possiblePrime holds the number to check for primality
	 * @param testNumber is the test number used for the test execution
	 * @return true if the test was passed, false if the test failed
	 */
	
	boolean runOnce(ImsInteger possiblePrime, ImsInteger testNumber){
		
		// if possiblePrime is really prime, its phi is its value-1
		ImsInteger exponent = possiblePrime.subtract(ImsInteger.ONE);

		// check if a^(p-1) mod p = 1, if yes return true
		return testNumber.modPow(exponent, possiblePrime).compareTo(ImsInteger.ONE)==0;
	}
}
