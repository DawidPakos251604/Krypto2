import org.junit.jupiter.api.Test;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ElGamalTest {

    @Test
    public void testKeyGeneration() {
        ElGamal.ElGamalKeyPair keys = ElGamal.generateKeys(512);
        assertNotNull(keys.p);
        assertNotNull(keys.g);
        assertNotNull(keys.a);
        assertNotNull(keys.h);

        assertTrue(keys.p.isProbablePrime(100), "p should be prime");
        assertTrue(keys.g.compareTo(BigInteger.ONE) > 0 && keys.g.compareTo(keys.p) < 0, "g should be in (1, p)");
    }

    @Test
    public void testSigningAndVerification() {
        ElGamal.ElGamalKeyPair keys = ElGamal.generateKeys(512);
        BigInteger message = new BigInteger("123456789");

        ElGamal.ElGamalSignature signature = ElGamal.sign(message, keys);

        assertNotNull(signature.s1);
        assertNotNull(signature.s2);

        assertTrue(ElGamal.verify(message, signature, keys));
    }

    @Test
    public void testVerificationFailsWithDifferentMessage() {
        ElGamal.ElGamalKeyPair keys = ElGamal.generateKeys(512);
        BigInteger message = new BigInteger("123456789");
        BigInteger alteredMessage = new BigInteger("987654321");

        ElGamal.ElGamalSignature signature = ElGamal.sign(message, keys);

        assertFalse(ElGamal.verify(alteredMessage, signature, keys));
    }

    @Test
    public void testVerificationFailsWithTamperedSignature() {
        ElGamal.ElGamalKeyPair keys = ElGamal.generateKeys(512);
        BigInteger message = new BigInteger("123456789");

        ElGamal.ElGamalSignature signature = ElGamal.sign(message, keys);
        signature.s2 = signature.s2.add(BigInteger.ONE);

        assertFalse(ElGamal.verify(message, signature, keys));
    }
}