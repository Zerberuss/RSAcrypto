package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import com.oracle.tools.packager.Log;

import java.util.Random;

public class RSA {
    private static int numOfPrimeTests = 40; // how often the primality test has to be passed in order to be accepted as a prime
    private static int informEveryNthNumber = 10000; // after that many trials to find a at.fhjoanneum.ims.rsacrypto.carmichael number, the user is informed


    private ImsInteger p;
    private ImsInteger q;
    private ImsInteger e;
    private int n;
    private ImsInteger d;
    private boolean optimized;

    public RSA(ImsInteger p, ImsInteger q, ImsInteger e) {
        this.p = p;
        this.q = q;
        this.e = q;
    }

    public RSA(int n, boolean optimized) {
        this.n = n;                                 //n is the bit length
        this.optimized = optimized;
    }

    private ImsInteger getP() {
        return p;
    }

    private ImsInteger getQ() {
        return q;
    }

    private ImsInteger getD() {
        return d;
    }

    public ImsInteger getE() {
        return e;
    }

    public int getN() {
        return n;
    }

    public void generatePQ() {
        //generate random numbers
        ImsInteger m = new ImsInteger(this.n/2, new Random());
        ImsInteger counter = ImsInteger.ZERO; // used to determine when to output information


        // increase m by one until all terms are prime
        do{
            counter=counter.add(ImsInteger.ONE);
            m = m.add(ImsInteger.ONE);

            // inform the user how many numbers we already checked
            if(counter.mod(ImsInteger.valueOf(informEveryNthNumber)).compareTo(ImsInteger.ZERO)==0)
            {
                System.out.println("Checked " + counter + " numbers, current basis: " + m);
            }

            p = ImsInteger.valueOf(1).multiply(m).add(ImsInteger.ONE);
            q = ImsInteger.valueOf(2).multiply(m).add(ImsInteger.ONE);


        }while(
                p.isProbablePrime(numOfPrimeTests) && q.isProbablePrime(numOfPrimeTests)
        );

        Log.info("Generated p: " +  p.toString() + ", q: " + q.toString());
        System.out.println("Generated p: " +  p.toString() + ", q: " + q.toString() + ", m: " + m.toString());
    }

}
