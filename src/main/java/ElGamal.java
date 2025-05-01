import java.math.BigInteger;
import java.security.SecureRandom;

public class ElGamal {

    static SecureRandom random = new SecureRandom();

    public static class ElGamalKeyPair {
        BigInteger p, g, a, h; // p, g, h - publiczne, a - prywatny
    }

    public static class ElGamalSignature {
        BigInteger s1, s2;

        public ElGamalSignature() {}

        public ElGamalSignature(BigInteger s1, BigInteger s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
    }

    // Generowanie kluczy
    public static ElGamalKeyPair generateKeys(int bitLength) {
        ElGamalKeyPair keyPair = new ElGamalKeyPair();

        keyPair.p = BigInteger.probablePrime(bitLength, random);
        keyPair.g = new BigInteger(bitLength - 2, random).mod(keyPair.p.subtract(BigInteger.ONE)).add(BigInteger.TWO);

        keyPair.a = new BigInteger(bitLength - 2, random).mod(keyPair.p.subtract(BigInteger.TWO)).add(BigInteger.ONE);
        keyPair.h = keyPair.g.modPow(keyPair.a, keyPair.p);

        return keyPair;
    }

    public static ElGamalSignature sign(BigInteger message, ElGamalKeyPair keys) {
        BigInteger pMinus1 = keys.p.subtract(BigInteger.ONE);
        BigInteger r;

        // Wybieranie r względnie pierwszej z p - 1
        do {
            r = new BigInteger(pMinus1.bitLength(), random);
        } while (!r.gcd(pMinus1).equals(BigInteger.ONE) || r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(pMinus1) >= 0);

        BigInteger s1 = keys.g.modPow(r, keys.p);
        BigInteger rInv = r.modInverse(pMinus1);
        BigInteger s2 = (message.subtract(keys.a.multiply(s1)).multiply(rInv)).mod(pMinus1);

        ElGamalSignature signature = new ElGamalSignature();
        signature.s1 = s1;
        signature.s2 = s2;
        return signature;
    }

    // Weryfikacja podpisu
    public static boolean verify(BigInteger message, ElGamalSignature signature, ElGamalKeyPair keys) {
        BigInteger v1 = keys.h.modPow(signature.s1, keys.p)
                .multiply(signature.s1.modPow(signature.s2, keys.p))
                .mod(keys.p);
        BigInteger v2 = keys.g.modPow(message, keys.p);
        return v1.equals(v2);
    }
}
