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
        String content = signature.s1.toString(16) + "\n" + signature.s2.toString(16);  // hex zapis
        Files.write(Paths.get(filePath), content.getBytes());
    }

    public static ElGamal.ElGamalSignature loadSignature(String filePath) throws Exception {
        String[] lines = new String(Files.readAllBytes(Paths.get(filePath))).split("\n");
        return new ElGamal.ElGamalSignature(
                new BigInteger(lines[0].trim(), 16),
                new BigInteger(lines[1].trim(), 16)
        );
    }

    public static void saveFullKey(String filePath, ElGamal.ElGamalKeyPair key) throws Exception {
        String content = key.p.toString(16) + "\n"
                + key.g.toString(16) + "\n"
                + key.h.toString(16) + "\n"
                + (key.a != null ? key.a.toString(16) : "");
        Files.write(Paths.get(filePath), content.getBytes());
    }


    public static ElGamal.ElGamalKeyPair loadFullKey(String filePath) throws Exception {
        String[] lines = new String(Files.readAllBytes(Paths.get(filePath))).split("\n");
        ElGamal.ElGamalKeyPair key = new ElGamal.ElGamalKeyPair();
        key.p = new BigInteger(lines[0].trim(), 16);
        key.g = new BigInteger(lines[1].trim(), 16);
        key.a = new BigInteger(lines[2].trim(), 16);
        key.h = key.g.modPow(key.a, key.p);

        return key;
    }


}
