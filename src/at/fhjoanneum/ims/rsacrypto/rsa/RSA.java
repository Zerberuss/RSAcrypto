package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import com.oracle.tools.packager.Log;

import java.util.Random;
import sun.awt.InputMethodSupport;

import java.math.BigInteger;

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

    public RSA(ImsInteger n, boolean optimized) {

        //generate primes    n > p*q;

        this.n = n;
        if (optimized) {
            //genreate pq
            //hardcoded e  2^16+1
            this.e = modPow(ImsInteger.valueOf(16), ImsInteger.ONE, ImsInteger.valueOf(2)).add(ImsInteger.ONE);
            this.d = generateDecryptionKey();
        } else {
            //generate pq
            //generate e
            this.d = generateDecryptionKey();

        }
        this.optimized = optimized;
    }

    private ImsInteger getP() {
        return p;
    }

    private ImsInteger generateDecryptionKey() {
        return (ImsInteger.ONE.divide(this.e)).getPhi();
    }

    private ImsInteger modPow(ImsInteger exponent, ImsInteger mod, ImsInteger base) {
        ImsInteger result = ImsInteger.ONE;
        for (int i = 0; i < exponent.bitLength(); i++) {
            if (exponent.testBit(i)) {
                result = (result.multiply(base)).mod(mod);
            }
            base = (base.multiply(base)).mod(mod);
        }
        return result.mod(mod);

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

    public ImsInteger encrypt(ImsInteger x, RSAKey pubKey) {
        //TODO: Replace pow
        return x.pow(pubKey.getExponent().getValue().intValue()).mod(pubKey.getModulus());
    }

    public ImsInteger decrypt(ImsInteger y) {
        //TODO: Replace pow
        return y.pow(d.getValue().intValue()).mod(q);
    }

    public ImsInteger decryptOptimized(ImsInteger y) {
        ImsInteger dp = d.mod(p.subtract(ImsInteger.ONE));
        ImsInteger dq = d.mod(q.subtract(ImsInteger.ONE));
        ImsInteger qInverse = inverse(q,p);

        ImsInteger m1 = y.modPow(dp, q);
        ImsInteger m2 = y.modPow(dq, p);
        ImsInteger h = qInverse.multiply(m1.subtract(m2)).mod(p);
        return m2.add(h.multiply(q));

    }

    private ImsInteger inverse(ImsInteger a, ImsInteger N) {
        ImsInteger[] ans = a.getBezout(N);
        if(ans[1].compareTo(ImsInteger.ZERO) == 1) {
            return ans[1];
        } else {
            return ans[1].add(N);
        }
    }
}
