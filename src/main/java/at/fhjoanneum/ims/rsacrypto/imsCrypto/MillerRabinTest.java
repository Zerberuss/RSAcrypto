package at.fhjoanneum.ims.rsacrypto.imsCrypto;


public class MillerRabinTest extends ProbabilityTest {
	private static final ImsInteger TWO = ImsInteger.valueOf(2);

	private boolean isExponentDividable(ImsInteger exponent) {
		ImsInteger mod = exponent.divide(TWO).mod(TWO);
		return mod.compareTo(ImsInteger.ZERO) == 0;
	}


	/**
	 * @param possiblePrime holds the number to check for primality
	 * @param testNumber is the test number used for the test execution
	 * @return true if the test was passed, false if the test failed
	 */

	boolean runOnce(ImsInteger possiblePrime, ImsInteger testNumber) {

		boolean success = true;

		// The initial step of Miller-Rabin is a Fermat test
		// the exponent p-1 is now consecutively halfed (see while loop)
		ImsInteger exponent = possiblePrime.subtract(ImsInteger.ONE);

		if(testNumber.modPow(exponent, possiblePrime).compareTo(ImsInteger.ONE)!=0){
			return false;
		}

		// while the exponent is even
		long counter = 0;
		while(exponent.testBit(0)==false){
			counter++;
			// take the squareroot, i.e. half the exponent
			exponent = exponent.shiftRight(1);

			////////////////////////////////////////////////////////
			// check the result of  a^(p-1) mod p
			////////////////////////////////////////////////////////
			ImsInteger result = (testNumber.modPow(exponent, possiblePrime));

			// check for -1 (i.e. p-1)
			if(result.compareTo(possiblePrime.subtract(ImsInteger.ONE))==0){
				// if the result is -1 abort with true
				success = true;
				break;

				// check if the result is something else than 1 or -1
			} else if(result.compareTo(ImsInteger.ONE)!=0){
				// if the result is neither 1 nor -1
				success = false;
				break;
			}

			// if we reach this line, the result is 1, thus do another round of Miller-Rabin
		}


		return success;
	}
}
