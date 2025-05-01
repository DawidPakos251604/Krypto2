import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileSignerTest {

    @TempDir
    Path tempDir;

    @Test
    public void testHashFile() throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        String content = "Hello, ElGamal!";
        Files.write(testFile, content.getBytes());

        BigInteger hash = FileSigner.hashFile(testFile.toString());
        assertNotNull(hash);
        assertTrue(hash.bitLength() > 0);
    }

    @Test
    public void testSaveAndLoadSignature() throws Exception {
        ElGamal.ElGamalSignature originalSig = new ElGamal.ElGamalSignature(
                new BigInteger("123456789"),
                new BigInteger("987654321")
        );

        Path sigFile = tempDir.resolve("signature.txt");
        FileSigner.saveSignature(sigFile.toString(), originalSig);

        ElGamal.ElGamalSignature loadedSig = FileSigner.loadSignature(sigFile.toString());

        assertEquals(originalSig.s1, loadedSig.s1);
        assertEquals(originalSig.s2, loadedSig.s2);
    }

    @Test
    public void testHashConsistency() throws Exception {
        Path testFile = tempDir.resolve("duplicate.txt");
        String content = "Same content";
        Files.write(testFile, content.getBytes());

        BigInteger hash1 = FileSigner.hashFile(testFile.toString());
        BigInteger hash2 = FileSigner.hashFile(testFile.toString());

        assertEquals(hash1, hash2);
    }

    @Test
    public void testDifferentFilesProduceDifferentHashes() throws Exception {
        Path file1 = tempDir.resolve("file1.txt");
        Path file2 = tempDir.resolve("file2.txt");
        Files.write(file1, "Content A".getBytes());
        Files.write(file2, "Content B".getBytes());

        BigInteger hash1 = FileSigner.hashFile(file1.toString());
        BigInteger hash2 = FileSigner.hashFile(file2.toString());

        assertNotEquals(hash1, hash2);
    }
}