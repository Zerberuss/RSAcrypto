package imsCrypto;

import java.util.Random;

public class CarMichaelCreator {
	
	private static int numOfPrimeTests = 40; // how often the primality test has to be passed in order to be accepted as a prime
	private static int informEveryNthNumber = 10000; // after that many trials to find a carmichael number, the user is informed

	public ImsInteger createCMNumber(int bits, boolean outputInfo){
		StopWatch watch = new StopWatch();
		watch.start();
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// How to produce a Carmichael Number?
		// Use the method of Chernick: 
		//
		// If 6m + 1, 12m + 1 und 18m + 1 are prime numbers, then (6m + 1)(12m + 1)(18m + 1) is a Carmichael-Number!
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		ImsInteger six = ImsInteger.valueOf(6);
		ImsInteger twelve = ImsInteger.valueOf(12);
		ImsInteger eighteen = ImsInteger.valueOf(18);
		
		ImsInteger m = new ImsInteger(bits, new Random());
		ImsInteger counter = ImsInteger.ZERO; // used to determine when to output information
		ImsInteger p1, p2, p3, phiCm;
		
		// increase m by one until all terms are prime
		do{
			counter=counter.add(ImsInteger.ONE);
			m=m.add(ImsInteger.ONE);
			
			// inform the user how many numbers we already checked
			if(outputInfo && counter.mod(ImsInteger.valueOf(informEveryNthNumber)).compareTo(ImsInteger.ZERO)==0)
			{
				System.out.println("Checked " + counter + " numbers, current basis: " + m);
			}
			
			p1 = six.multiply(m).add(ImsInteger.ONE);
			p2 = twelve.multiply(m).add(ImsInteger.ONE);
			p3 = eighteen.multiply(m).add(ImsInteger.ONE); 
			
		}while( !(
				p1.isProbablePrime(numOfPrimeTests) &&
				p2.isProbablePrime(numOfPrimeTests) &&
				p3.isProbablePrime(numOfPrimeTests)
				) );
		
		// the carmichael number is found!
		ImsInteger carmichael = p1.multiply( p2 ).multiply( p3 );
		watch.stop();
		
		// calculate phi(carmichael) and charmichael-phi to output probability information
		phiCm = p1.subtract(ImsInteger.ONE).multiply(p2.subtract(ImsInteger.ONE)).multiply(p3.subtract(ImsInteger.ONE));
		ImsInteger antiPhiCm = carmichael.subtract(ImsInteger.ONE).subtract(phiCm);
		
		if(outputInfo){
			System.out.println("Found Carmichael-Number in " + watch.getElapsedTime() + " ms:");
			System.out.println("Basis used:                                " + m);
			System.out.println("Carmichael.Number (6m+1)(12m+1)(18m+1): " + carmichael);
			System.out.println("There are " + antiPhiCm + " numbers for which a fermat test will fail because they have a common divisor with your Carmichael");
			System.out.println("There are " + phiCm + " numbers for which a fermat test will succeed because they have no common divisor with your Carmichael number");
			System.out.println("They are called wrong witnesses for the primality of your Carmichael number");		
			System.out.println("\nThe chance that a fermat test fails is thus:  1/" + carmichael.subtract(ImsInteger.ONE).divide(antiPhiCm));
		}
		watch.reset();
		
		return carmichael;
	}
}
