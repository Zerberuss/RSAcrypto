package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;


public class RSA {

    private ImsInteger p;
    private ImsInteger q;
    private ImsInteger e;
    private ImsInteger n;
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

    public ImsInteger getN() {
        return n;
    }
}
