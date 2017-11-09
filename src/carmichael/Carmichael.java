package carmichael;

import imsCrypto.ImsInteger;
import imsCrypto.CarMichaelCreator;
import imsCrypto.MillerRabinTest;
import imsCrypto.ProbabilityTest;

import java.util.Scanner;



public class Carmichael {
	private static int minBits = 4;
	private static long minFermatTests = 1;
	private static boolean outputInfo = true;
	
	public static void main(String[] args) {
		try( final Scanner in = new Scanner(System.in) ){
		
			System.out.println("This programme initially creates a Carmichael Number which is made up of exactly three prime factors.");
			
			//////////////////////////////////////////////////////
			// create a Carmichael number
			//////////////////////////////////////////////////////
			
			Integer bits = null;
			do{
				System.out.println("\nPlease specify the number of bits the three prime factors shall be made up of (minimum of " + minBits + "):");
				bits = new Integer(in.nextLine());
			}while(bits < minBits);
				
			CarMichaelCreator cmCreator = new CarMichaelCreator();
			ImsInteger carMichael = cmCreator.createCMNumber(bits, outputInfo);
			
			// do a series of Fermat Test to demonstrate the danger of Carmichael numbers
			long tests = 0;
			do{
				System.out.println("\nPlease specify the number of Miller-Rabin tests you want to execute (minimum of " + minFermatTests + "):");
				tests = new Long(in.nextLine());
			}while(tests < minFermatTests);
			
			ProbabilityTest test = new MillerRabinTest();
			
			System.out.println("\nDoing Miller-Rabin tests unitl one witnesses the compositness of your Carmichael number or " + tests + " tests succeed.");
			boolean isPrime = test.run(carMichael, tests, outputInfo);
			if(isPrime){
				System.out.println("Your Miller-Rabin tests did not detect that your Carmichael number is not prime!");
			} else {
				System.out.println("Your Miller-Rabin test detected the compositness of your Carmichael number!");
			}
		} finally {
			System.out.println("Program finished...");
		}
	}
}
