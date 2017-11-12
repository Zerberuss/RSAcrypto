package at.fhjoanneum.ims.rsacrypto;

import at.fhjoanneum.ims.rsacrypto.imsCrypto.ImsInteger;
import at.fhjoanneum.ims.rsacrypto.imsCrypto.StopWatch;
import at.fhjoanneum.ims.rsacrypto.rsa.RSA;
import at.fhjoanneum.ims.rsacrypto.rsa.RSAKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RSAPerformanceTest {

    public static void main(String[] args) {
        try(final Scanner in = new Scanner(System.in)) {
            System.out.println("Please enter the number of bits");
            Integer bits = new Integer(in.nextLine());

            System.out.println("Please enter y if the system should be optimized or n if not");
            String optimized = in.nextLine();

            System.out.println("Please enter the number of test runs");
            Integer testRuns = new Integer(in.nextLine());

            RSA rsaObject;

            if("y".equals(optimized)) {
                rsaObject = new RSA(bits, true);
            } else if("n".equals(optimized)){
                rsaObject = new RSA(bits, false);
            } else {
                System.out.println("Wrong input");
                return;
            }


            List<ImsInteger> encodedMessages = new ArrayList<>();

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            for (int i = 0; i < testRuns; i++) {
                ImsInteger randomNumber = new ImsInteger(bits, new Random());
                encodedMessages.add(rsaObject.encrypt(randomNumber, new RSAKey(ImsInteger.valueOf(rsaObject.getN()), rsaObject.getE())));

            }
            stopWatch.stop();
            System.out.println(String.format("Encryption of %d messages took %d ms", testRuns, stopWatch.getElapsedTime()));

            stopWatch.reset();
            stopWatch.start();
            for(ImsInteger message: encodedMessages) {
                rsaObject.decrypt(message);
            }
            stopWatch.stop();
            System.out.println(String.format("Decryption of %d messaged took %d ms", testRuns, stopWatch.getElapsedTime()));

        }
    }
}
