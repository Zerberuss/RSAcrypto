package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import at.fhjoanneum.ims.rsacrypto.imsCrypto.MillerRabinTest;

import java.util.Random;


public class RSA {
    private static int NUM_OF_PRIME_TESTS = 44;

    private ImsInteger p;
    private ImsInteger q;
    private ImsInteger e;
    private ImsInteger n;
    private ImsInteger d;

    public RSA(ImsInteger p, ImsInteger q, ImsInteger e) {
        this.p = p;
        this.q = q;
        this.e = e;
        this.n = p.multiply(q);
        this.d = generateDecryptionKey();
    }

    public RSA(int bitLength, boolean optimized) {

        generatePQ(bitLength);
        this.n = p.multiply(q);


        ImsInteger phiN = getPhiN();

        if (optimized) {
            this.e = ImsInteger.valueOf(2).pow(16).add(ImsInteger.ONE);

        } else {

            do {
                e = new ImsInteger(phiN.bitLength(), new Random());
            } while (e.compareTo(ImsInteger.ZERO) == 0 || e.compareTo(phiN) >= 0 || e.getGcd(phiN).compareTo(ImsInteger.ONE) != 0);

        }

        this.d = generateDecryptionKey();
    }

    public ImsInteger encrypt(ImsInteger x, RSAKey pubKey) {
        return x.modPow(pubKey.getExponent(), pubKey.getModulus());
    }

    public ImsInteger decrypt(ImsInteger y) {
        return y.modPow(d, n);
    }

    public ImsInteger decryptOptimized(ImsInteger y) {
        ImsInteger dp = d.mod(p.subtract(ImsInteger.ONE));
        ImsInteger dq = d.mod(q.subtract(ImsInteger.ONE));
        ImsInteger qInverse = (q.getBezout(p))[0];

        ImsInteger m1 = y.modPow(dp, p);
        ImsInteger m2 = y.modPow(dq, q);
        ImsInteger h = qInverse.multiply(m1.subtract(m2)).mod(p);
        return m2.add(h.multiply(q));
    }

    private ImsInteger getP() {
        return p;
    }

    private ImsInteger generateDecryptionKey() {

        ImsInteger dKey =  this.e.getBezout(getPhiN())[0];
        if (dKey.compareTo(ImsInteger.ZERO) < 0) {
            dKey = dKey.mod(getPhiN());
        }

        return dKey;

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

    public ImsInteger getN() {
        return n;
    }

    public ImsInteger generatePrime(Integer bitLength) {

        MillerRabinTest millerTest = new MillerRabinTest();
        //generate random numbers
        bitLength = bitLength/2;

        ImsInteger m;

        // increase m by one until all terms are prime
        do{
            m = new ImsInteger(bitLength, new Random());

        } while (m.compareTo(ImsInteger.ZERO) <= 0 || !millerTest.run(m, NUM_OF_PRIME_TESTS, false));

        return m;
    }


    private void generatePQ(int bitLength) {
        this.q = generatePrime(bitLength);

        do{
            this.p = generatePrime(bitLength);
            this.n = p.multiply(q);
        } while (p.compareTo(q) == 0);

    }


    private ImsInteger getPhiN() {
        return p.subtract(ImsInteger.ONE).multiply(q.subtract(ImsInteger.ONE));
    }



}
