package auth;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Genererar randomsträngar
 */
public class RandomGenerator {

    public static String generateRegLink() {
       return generateRandom(32);
    }
    public static String generateRandomPassword() {
        return generateRandom(4);
    }
    private static String generateRandom(int bytes){
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[bytes];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16);
    }

}
