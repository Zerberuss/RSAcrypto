package at.fhjoanneum.ims.rsacrypto.imsCrypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Random;

/**
 * 
 * Class AseInteger implements the most important methods of BigInteger and adds the algorithms of our semester as methods
 * BigInteger is used as a delegate for the implementation of the BigInteger methods, so that the return values and parameters are all AseIntegers
 *
 */
public class ImsInteger implements Comparable<ImsInteger> {
	
	public static final ImsInteger ZERO = new ImsInteger(BigInteger.ZERO);
	public static final ImsInteger ONE = new ImsInteger(BigInteger.ONE);
	public static final ImsInteger TEN = new ImsInteger(BigInteger.TEN);
	
	private BigInteger myValue;

	private BigInteger getMyValue() {
		return myValue;
	}

	private void setMyValue(BigInteger myValue) {
		this.myValue = myValue;
	}

	public ImsInteger(BigInteger val){
		setMyValue(val);
	}
	
	public ImsInteger(byte[] val) {
		setMyValue( new BigInteger(val) );
	}
		

	public ImsInteger(int signum, byte[] magnitude) {
		setMyValue( new BigInteger(signum, magnitude) );
	}


	public ImsInteger(int bitLength, int certainity, Random rnd) {
		setMyValue( new BigInteger(bitLength, certainity, rnd) );
	}

	
	// create a random AseInteger
	public ImsInteger(int numBits, Random rnd) {
		setMyValue( new BigInteger(numBits, rnd) );
	}

	
	// create a random prime AseInteger
	public ImsInteger(int numBits) {
		setMyValue( BigInteger.probablePrime(numBits, new Random()) );
	}

	
	public ImsInteger(String val, int radix) {
		setMyValue( new BigInteger(val, radix) );
	}


	public ImsInteger(String val) {
		setMyValue( new BigInteger(val) );
	}
	

	public ImsInteger add(ImsInteger arg0) {
		return new ImsInteger( this.getMyValue().add(arg0.getMyValue() ) );
	}
	
	public static ImsInteger valueOf(long i){
		return  new ImsInteger( BigInteger.valueOf(i));
	}
	
	public int compareTo(ImsInteger arg0) {
		return this.getMyValue().compareTo(arg0.getMyValue());
	}

	// standard integer division yielding quotient only
	public ImsInteger divide(ImsInteger arg0) {
		return new ImsInteger( this.getMyValue().divide(arg0.getMyValue()) );
	}

	// standard integer division yielding quotient and remainder
	public ImsInteger[] divideAndRemainder(ImsInteger arg0) {
		BigInteger[] res = this.getMyValue().divideAndRemainder(arg0.getMyValue());
		ImsInteger[] ret = new ImsInteger[res.length];
		for(int i=0; i<res.length; i++){
			ret[i]=new ImsInteger( res[i] );
		}
		return ret;
	}

	// standard multiplication operation
	public ImsInteger multiply(ImsInteger arg0) {
		return new ImsInteger( this.getMyValue().multiply(arg0.getMyValue()) );
	}

	// standard substraction operation
	public ImsInteger subtract(ImsInteger arg0) {
		return new ImsInteger( this.getMyValue().subtract(arg0.getMyValue()) );
	}
	
	// standard exponentiation operation
	public ImsInteger pow(int exponent){
		return new ImsInteger( this.getMyValue().pow(exponent));
	}
	
	// standard modulus operation
	public ImsInteger mod(ImsInteger modNum){
		return new ImsInteger( this.getMyValue().mod(modNum.getMyValue()) );
	}
	
	// test wether the bit specified in the paramters is set to 1 (least significant bit has position 0)
	public boolean testBit(int bit){
		return this.getMyValue().testBit(bit);
	}
	
	public ImsInteger setBit(int position){
		return new ImsInteger( this.getMyValue().setBit(position));
		
	}
	
	public int bitCount(){
		return this.getMyValue().bitCount();
	}
	
	public int bitLength(){
		return this.getMyValue().bitLength();
	}
	
	// bit-shift the AseInteger right by the number of bits specified in the parameter
	public ImsInteger shiftRight(int bits){
		return new ImsInteger(this.getMyValue().shiftRight(bits));
	}
	
	// bit-shift the AseInteger left by the number of bits specified in the parameter
	public ImsInteger shiftLeft(int bits){
		return new ImsInteger(this.getMyValue().shiftLeft(bits));
	}
	
	// test AseInteger for primality
	public boolean isProbablePrime(int numOfTests){
		return this.getMyValue().isProbablePrime(numOfTests);
	}
	
	// get the next prime number following the given AseInteger 
	// implicitely calls isProbablePrime()
	public ImsInteger nextProbablePrime() {
		return new ImsInteger( this.getMyValue().nextProbablePrime() );
	}
	
	/**
	 * modPow() exponentiation of this. 
	 * 		 We replace the exponentiation function's implementation found in BigInteger,
	 *       by our own smart implementation
	 * 
	 * @param exponent - the exponent is now an AseInteger instead of an int such that we can do huge exponentiations
	 * @return An AseInteger holding the gcd.
	 */
	public ImsInteger modPow(ImsInteger exponent, ImsInteger mod){
		// For each bit of the exponent, we square the basis to get a "squared result", 
		// If the bit is 1, we multiply our intermediate result with the "squared result"
		// This will provide us with the final result after working through all bits
				
		ImsInteger squaredResult = this;
		ImsInteger intermediateResult = ImsInteger.ONE;
				
		do{
			if(exponent.testBit(0)){
				intermediateResult = intermediateResult.multiply(squaredResult).mod(mod);
			}
					
			// prepare for the next run
			squaredResult = squaredResult.multiply(squaredResult).mod(mod);
			exponent = exponent.shiftRight(1);
		}while(exponent.compareTo(ImsInteger.ZERO) >0);
		return intermediateResult;
	}
	
	public String toString(){
		return this.getMyValue().toString();
	}

	
	/**
	 * getGcd() returns the gcd of the current and another numbers using the Euclidean Algorithm
	 * 
	 * @param b The other number, the gcd of this and b is calculated 
	 * @return An AseInteger holding the gcd.
	 */
	public ImsInteger getGcd(ImsInteger b) {
		ImsInteger a = this;
		ImsInteger q,r;
		
		// make sure that a is the dividend and b the divisor
		if( this.compareTo(b) < 0 ){ // if a < b, then just switch a and b
			a = b ;
			b = this;
		}
		
		// iterate over all steps of the algorithm
		do{
			// a=bq+r
			ImsInteger[] res = a.divideAndRemainder(b); 
			q=res[0];
			r=res[1];
			
			// output the numbers in a=bq+r format to the screen, so that the user can view the algorithm running step by step
			System.out.println(a + "=" + b + "*" + q + "+" + r);
			
			// b becomes your new a, r becomes your new b
			a = b; // prepare the next row of the algorithm
			b = r;
			
			// iterate until no remainder is left
		}while(r.compareTo(ImsInteger.ZERO) != 0);
		
		// the gcd is the b of the last division - which was already moved to a
		return a;
	}
	
	/**
	 * getBezout() returns the factors x and y of bezouts equation as well as the gcd for the two integers this and b
	 * 	
	 * a*x + b*y = gcd
	 * 
	 * @param b The other integer, the bezout factors x and y are calculated for this (x) and b (y) 
	 * @return An array of three AseInteger holding x (element 0) and y (element 1) and the gcd (element 2)
	 *
	 */
	public ImsInteger[] getBezout(ImsInteger b) {
		ImsInteger a = this;
		ImsInteger q,r;
		
		List<ImsInteger> negQuotients = new ArrayList<>();
		List<ImsInteger> outputNumbers = new ArrayList<>();
		
		// iterate over all steps of the algorithm, creating the Bezout equations on the fly
		do{
			// a = bq  + r 
			// r = 1*a -(q)*b -> for substitution step in extension we have to store a and -q
			ImsInteger[] res = a.divideAndRemainder(b);
			q=res[0];
			r=res[1];
			ImsInteger negQuotient = ImsInteger.ZERO.subtract(q);
			negQuotients.add(0, negQuotient); // add intermediate results at top of list
			outputNumbers.add(0, a);
			
			// output the numbers in a=bq+r format to the screen, so that the user can view the algorithm running step by step
			System.out.println(a + "=" + b + "*" + q + "+" + r);
			
			// b becomes your new a, r becomes your new b
			a = b; // prepare the next row of the algorithm
			b = r;
			
			// iterate until no remainder is left
		}while(r.compareTo(ImsInteger.ZERO) != 0);
		
		// the gcd is the b of the last division - which was already moved to a
		ImsInteger gcd = a;
		System.out.println("\ngcd="+gcd+"\n");
		
		// now iterate over the substitutions of the remainders to get Bezouts equation
		Iterator<ImsInteger> iter = negQuotients.iterator();
		
		// Last step of Euclid: b=rq + 0 -> r is the gcd, this quotient q is not needed for Bezout!
		iter.next();
		
		// for b = rq + 0 we get the initial Bezout equation
		// -> gcd = b*0 + r*1
		ImsInteger x = ImsInteger.ZERO;
		ImsInteger y = ImsInteger.ONE;
				
		// prepare nice output
		Iterator<ImsInteger> outputIter = outputNumbers.iterator();
		ImsInteger bOutput = gcd;
		ImsInteger aOutput = outputIter.next();
		
		while(iter.hasNext()){
			// a = bq+r
			// get the -q from that equation
			ImsInteger minusQ = iter.next();
			bOutput = aOutput;
			aOutput = outputIter.next();
			
			// gcd = b*x + r*y -> substitute r with r = a - bq 
			// -> gcd = b*x + (a-bq)*y
			// -> gcd = a*y + b*(x-qy) -> X = y, Y = x-qy
			ImsInteger Y = x.add(minusQ.multiply(y));
			ImsInteger X = y;
			System.out.println(gcd + "=" + aOutput + "*" + X + "+" + bOutput + "*" + Y);
			x=X;
			y=Y;
		}
		
		ImsInteger[] retArray = new ImsInteger[3];
		retArray[0]=x;
		retArray[1]=y;
		retArray[2]=gcd;
		return retArray;
	}
	
	/**
	 * getGcdRec() returns the gcd of the current and another numbers using the Euclidean Algorithm in recursive fashion
	 * 
	 * @param b The other number, the gcd of this and b is calculated 
	 * @return An AseInteger holding the gcd.
	 */
	public ImsInteger getGcdRec(ImsInteger b) {
		ImsInteger a = this;
		
		// make sure that a is the dividend and b the divisor
		if( this.compareTo(b) < 0 ){ // if a < b, then just switch a and b
			a = b ;
			b = this;
		}
		
		// a=bq+r
		ImsInteger[] res = a.divideAndRemainder(b);
		
		// are we finished? (remainder == zero)
		if(res[1].compareTo(ImsInteger.ZERO) == 0){
			
			// then return the last divisor
			return b;
		} else {
			
			// otherwise return gcd(b,r) which we know to be equal to gcd(a,b)
			return b.getGcdRec(res[1]);
		}
	}
	
	/**
	 * getBezoutRec() returns the factors x and y of bezouts equation as well as the gcd for the two integers this and b in recursive fashion
	 * 
	 * @param b The other integer, the bezout factors x and y are calculated for this (x) and b (y) 
	 * @return An array of three AseInteger holding x (element 0) and y (element 1) and the gcd (element 2)
	 */
	public ImsInteger[] getBezoutRec(ImsInteger b) {
		
		ImsInteger[] retArray = new ImsInteger[3];
		ImsInteger a = this;
		
		// a=bq+r
		ImsInteger[] res = a.divideAndRemainder(b);
		ImsInteger q = res[0];
		ImsInteger r = res[1];
		
		// are we finished? (remainder == zero)
		if(r.compareTo(ImsInteger.ZERO) == 0){
			// a = bq -> b is the gcd, b = a*0 + b*1
			ImsInteger x = ImsInteger.ZERO;
			ImsInteger y = ImsInteger.ONE;
			retArray[0] = x;
			retArray[1] = y;
			retArray[2] = b;
		} else {
			// get the result of the extended euclidean algorithm for (b,r) -> gcd = b*x + r*y
			retArray = b.getBezoutRec(r);
			ImsInteger x = retArray[0];
			ImsInteger y = retArray[1];
			ImsInteger gcd = retArray[2];
			
			// gcd = b*x + r*y AND r = a + (-q)b -> gcd = b*x + ( a+(-q)b)y 
			//-> gcd = a*y + b*x + b*(-q)y -> gcd = a*y + b*(x+y(-q)) -> X=y, Y = x-qy
			ImsInteger X = y;
			ImsInteger Y = x.subtract(q.multiply(y));
			retArray[0] = X;
			retArray[1] = Y;
			retArray[2] = gcd;
		}
		return retArray;
	}
	
	public ImsInteger getGcdFact(ImsInteger b) {
		ImsInteger a = this;
		NavigableMap<ImsInteger, Integer> aPrimeFactors = a.getPrimeFactors();
		NavigableMap<ImsInteger, Integer> bPrimeFactors = b.getPrimeFactors();
		ImsInteger gcd = ImsInteger.ONE;
		
		Entry<ImsInteger, Integer> entry = aPrimeFactors.firstEntry();
		do{
			Integer exponentB = bPrimeFactors.get(entry.getKey());
			ImsInteger prime = entry.getKey();
			Integer exponentA = entry.getValue();
			
			if(exponentB != null){
				if( Integer.compare(exponentA, exponentB) < 0)
				{
					gcd = gcd.multiply( prime.pow( exponentA ) );
					System.out.println("prime: " + prime + " exponent: " + exponentA);
				} else {
					gcd = gcd.multiply( prime.pow( exponentB ) );
					System.out.println("prime: " + prime + " exponent: " + exponentB);
				}
			}
			entry = aPrimeFactors.higherEntry(prime);
		}while(entry != null);
		
		return gcd;
	}
	
	/**
	 * getPrimeFactors() calculates the prime factorization of this
	 * 
	 * @return Returns the prime factorization in a key value map, the keys being the primes, the values being the exponents
	 */
	public java.util.NavigableMap<ImsInteger, Integer> getPrimeFactors(){
		NavigableMap <ImsInteger, Integer> retmap = new TreeMap<>();
		ImsInteger number = this;
		ImsInteger prime = new ImsInteger("2");
		Integer exponent = 0;
		
		while(prime.multiply(prime).compareTo(number)<=0){
			ImsInteger[] result = number.divideAndRemainder(prime);
			ImsInteger quotient = result[0];
			ImsInteger remainder = result[1];
			if(remainder.compareTo(ImsInteger.ZERO) == 0){ // prime did divide number
				number = quotient;
				exponent = new Integer( exponent.intValue()+1 );
				retmap.put(prime, exponent);
			} else {
				exponent = new Integer(0);
				prime = prime.nextProbablePrime();
			}
		}
		if(prime.compareTo(number) == 0){
			exponent = new Integer( exponent.intValue()+1 );
			retmap.put(number, exponent);
		} else {
			retmap.put(number, 1);
		}
		
		System.out.println("Retmap" + retmap);
		return retmap;
	}
	
	// calculate phi(this) using the getPrimeFactors method
	public ImsInteger getPhi(){
		ImsInteger phi = ImsInteger.ONE;
		java.util.NavigableMap<ImsInteger, Integer> primeFactors = this.getPrimeFactors();
		Entry<ImsInteger, Integer> primePlusExponent = primeFactors.firstEntry();
		while(primePlusExponent != null){
			final ImsInteger prime = primePlusExponent.getKey();
			final int exp = primePlusExponent.getValue().intValue();
			phi = phi.multiply(prime.pow(exp-1).multiply(prime.subtract(ImsInteger.ONE)));
			primePlusExponent = primeFactors.higherEntry(prime);
		}
			
		return phi;
	}
}
