package at.fhjoanneum.ims.rsacrypto.rsa;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import static org.junit.Assert.*;

public class RSATest {

    @Test
    public void RSAEncryptionShouldWork() {
        RSA rsa = new RSA(ImsInteger.valueOf(11), ImsInteger.valueOf(13), ImsInteger.valueOf(23));
        ImsInteger c = rsa.encrypt(ImsInteger.valueOf(7), new RSAKey(rsa.getN(), rsa.getE()));
        assertEquals(ImsInteger.valueOf(2), c);
    }

    @Test
    public void RSADecryptionShouldWork() {
        RSA rsa = new RSA(ImsInteger.valueOf(11), ImsInteger.valueOf(13), ImsInteger.valueOf(23));

        ImsInteger e = rsa.decrypt(ImsInteger.valueOf(2));

        assertEquals(ImsInteger.valueOf(7), e);
    }


    @Test
    public void RSAOptimizedDecryptionShouldWork() {
        RSA rsa = new RSA(ImsInteger.valueOf(11), ImsInteger.valueOf(13), ImsInteger.valueOf(23));

        ImsInteger e = rsa.decryptOptimized(ImsInteger.valueOf(2));

        assertEquals(ImsInteger.valueOf(7), e);
    }

}
