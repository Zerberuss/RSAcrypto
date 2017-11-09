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

		// The initial step of Miller-Rabin is a Fermat test
		ImsInteger exponent = possiblePrime.subtract(ImsInteger.ONE);
		ImsInteger minusOne = ImsInteger.valueOf(-1).mod(possiblePrime);

		do {
			ImsInteger solution = testNumber.modPow(exponent, possiblePrime);


			if(solution.compareTo(minusOne) == 0) {
				return true;
			}
			if(solution.compareTo(minusOne) != 0 && solution.compareTo(ImsInteger.ONE) != 0) {
				return false;
			}
			if(solution.compareTo(ImsInteger.ONE) == 0) {
				boolean isDividable = isExponentDividable(exponent);
				if(isDividable) {
					exponent = exponent.divide(TWO);
				} else {
					return true;
				}
			}

		} while (exponent.mod(TWO).compareTo(ImsInteger.ZERO) == 0);

		return false;
	}
}
