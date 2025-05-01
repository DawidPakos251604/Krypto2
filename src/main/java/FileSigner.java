import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class FileSigner {

    public static BigInteger hashFile(String filePath) throws Exception {
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        return new BigInteger(1, hash);
    }

    public static void saveSignature(String filePath, ElGamal.ElGamalSignature signature) throws Exception {
        String content = signature.s1.toString() + "\n" + signature.s2.toString();
        Files.write(Paths.get(filePath), content.getBytes());
    }

    public static ElGamal.ElGamalSignature loadSignature(String filePath) throws Exception {
        String[] lines = new String(Files.readAllBytes(Paths.get(filePath))).split("\n");
        return new ElGamal.ElGamalSignature(new BigInteger(lines[0]), new BigInteger(lines[1]));
    }
}
