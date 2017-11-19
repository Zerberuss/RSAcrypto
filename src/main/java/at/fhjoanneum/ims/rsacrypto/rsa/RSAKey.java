package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;

public class RSAKey {
    private ImsInteger modulus;
    private ImsInteger exponent;

    public RSAKey(ImsInteger modulus, ImsInteger exponent) {
        this.modulus = modulus;
        this.exponent = exponent;
    }

    public ImsInteger getModulus() {
        return modulus;
    }


    public ImsInteger getExponent() {
        return exponent;
    }

}
