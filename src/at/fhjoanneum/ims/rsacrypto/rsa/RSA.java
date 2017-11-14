package at.fhjoanneum.ims.rsacrypto.rsa;
import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import at.fhjoanneum.ims.rsacrypto.imsCrypto.MillerRabinTest;

import java.util.Random;


public class RSA {
    private static int numOfPrimeTests = 44; // how often the primality test has to be passed in order to be accepted as a prime

    private ImsInteger p;
    private ImsInteger q;
    private ImsInteger e;
    private Integer n;
    private ImsInteger d;
    private boolean optimized;

    public RSA(ImsInteger p, ImsInteger q, ImsInteger e) {
        this.p = p;
        this.q = q;
        this.e = q;
    }

    public RSA(Integer n, boolean optimized) {

        this.n = n;
        generatePQ();
        //generate primes    n > p*q;


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

    public ImsInteger generatePrime(Integer n) {

        MillerRabinTest millerTest = new MillerRabinTest();
        //generate random numbers
        n = n/2;
        ImsInteger m = new ImsInteger(n, new Random());
        System.out.println("New " + n + "bit random: " + m.getValue());

        ImsInteger prime;

        // increase m by one until all terms are prime
        do{
            m = m.add(ImsInteger.ONE);

            prime = ImsInteger.ONE.multiply(m).add(ImsInteger.ONE);

        }while (millerTest.run(prime, numOfPrimeTests, false) != true);

        return prime;
    }

    public void generatePQ() {
        q = generatePrime(this.n);

        do{
            p = generatePrime(this.n);
        } while (p.compareTo(q) == 0);

        System.out.println("Generated p: " +  p.toString() + ", q: " + q.toString() + ", p*q => " + q.multiply(p).bitLength()+ " bits");
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
