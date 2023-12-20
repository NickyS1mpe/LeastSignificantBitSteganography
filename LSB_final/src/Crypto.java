import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

//密码类应用程序
public class Crypto {

    //AES加密
    //采用ECB电码本模式进行加密，采用填充
    public String Encrypt_AES(String mes, String key) {
        if (key == null || mes == null) {
            System.out.println("encrypt error");
            return null;
        }

        if (key.length() != 16) {
            System.out.println("the length of key is not 16");
            return null;
        }

        byte[] keys = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keys, "AES");

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(mes.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //AES解密
    //采用ECB电码本模式进行解密，采用填充
    public String Decrypt_AES(String mes, String key) {
        if (key == null || mes == null) {
            System.out.println("decrypt error");
            return null;
        }
        if (key.length() != 16) {
            System.out.println("the length of key is not 16");
            return null;
        }
        byte[] keys = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keys, "AES");

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(mes));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //伪随机列表生成，只包含0和1
    //采用SHA-256信息摘要对密钥进行加密，将加密结果用于生成随机序列的种子值
    public static List<Integer> generateRandomList(int length, String key) {
        List<Integer> bitList = new ArrayList<>();
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }

        byte[] digest = md.digest(keyBytes);
        Random random = new Random(toInt(digest));

        for (int i = 0; i < length; i++) {
            int bit = random.nextInt(2);
            bitList.add(bit);
        }
        return bitList;
    }

    //将字符流转换为整形
    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result += (bytes[i] & 0xff) << (8 * i);
        }
        return result;
    }
}
