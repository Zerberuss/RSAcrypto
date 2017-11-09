package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import sun.awt.InputMethodSupport;

import java.math.BigInteger;

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
        this.n = n;
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

    public ImsInteger getN() {
        return n;
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
