package at.fhjoanneum.ims.rsacrypto;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import at.fhjoanneum.ims.rsacrypto.rsa.RSA;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world");

        RSA rsaObject = new RSA(1000, false);

        rsaObject.generatePQ();

    }
}
