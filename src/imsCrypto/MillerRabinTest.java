package imsCrypto;


public class MillerRabinTest extends ProbabilityTest{

	/**
	 * @param possiblePrime holds the number to check for primality
	 * @param testNumber is the test number used for the test execution
	 * @return true if the test was passed, false if the test failed
	 */
	
	boolean runOnce(ImsInteger possiblePrime, ImsInteger testNumber){		//n, a

		// check if a^(p-1) mod p = 1, if yes return true

		// if possiblePrime is really prime, its phi is its value-1
		ImsInteger exponent = possiblePrime.subtract(ImsInteger.ONE);		//n-1
		ImsInteger minusOne = ImsInteger.valueOf(-1);
		ImsInteger minusOneCheck = minusOne.mod(possiblePrime);

		       //a^expo (=n-1) mod possiblePrime
			do {
				if( testNumber.modPow(exponent, possiblePrime).compareTo(ImsInteger.ONE)==0)
					exponent = exponent.mod(ImsInteger.valueOf(2));
				else
					return false;

			} while (exponent.compareTo(ImsInteger.ZERO) != 0 || exponent.compareTo(minusOneCheck) != 0);        //-1 is immer positiv

			if (exponent.compareTo(minusOneCheck) == 0) {
				return false;
			} else if (exponent.compareTo(ImsInteger.ZERO) == 0)
				return true;
			else
				return false;





		
		// The initial step of Miller-Rabin is a Fermat test
		
		
		// the exponent p-1 is now consecutively halfed solang bis 1 oder -1 rasukommt oder gerade
		
		
		// while the exponent is even
				

	}
}
