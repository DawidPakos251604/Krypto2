import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {


    @Test
    public void testPrimeNumbers() throws Exception {

        assertFalse(Utils.isPrime(BigInteger.TEN));
        assertTrue(Utils.isPrime(BigInteger.TWO));

        BigInteger n = BigInteger.valueOf(7919);
        assertTrue(Utils.isPrime(n));


    }


}
