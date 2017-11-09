package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;

public class RSA {
    private ImsInteger p;
    private ImsInteger q;
    private ImsInteger e;
    private ImsInteger n;
    private ImsInteger d;

    public RSA(ImsInteger p, ImsInteger q, ImsInteger e) {
        this.p = p;
        this.q = q;
        this.e = q;
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
}
