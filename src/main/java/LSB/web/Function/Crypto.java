package LSB.web.Function;

import LSB.web.Controller.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName: Crypto
 * @Description: 密码类，AES加解密和随机列表生成
 * @Author: Nick Lee
 * @Date: Create in 18:03 2023/3/11
 **/
public class Crypto {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    /**
     * @Author: Nick Lee
     * @Description: AES加密
     * @Date: 2023/3/26 0:44
     * @Return: 加密结果
     **/
    public String Encrypt_AES(String mes, String key) {
        if (key == null || mes == null) {
            LOGGER.info("encrypt error");
            return null;
        }
        if (key.length() != 16) {
            LOGGER.info("the length of key is not 16");
            return null;
        }
        byte[] keys = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keys, "AES");

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(mes.getBytes(StandardCharsets.UTF_8));
            return Base64Utils.encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: Nick Lee
     * @Description: AES解密
     * @Date: 2023/3/26 0:44
     * @Return: 解密结果
     **/
    public String Decrypt_AES(String mes, String key) {
        if (key == null || mes == null) {
            LOGGER.info("decrypt error");
            return null;
        }
        if (key.length() != 16) {
            LOGGER.info("the length of key is not 16");
            return null;
        }
        byte[] keys = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keys, "AES");

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(Base64Utils.decodeFromString(mes));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: Nick Lee
     * @Description: 伪随机列表生成，只包含0和1
     * @Date: 2023/3/25 1:41
     * @Return: 伪随机列表
     **/
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

    /**
     * @Author: Nick Lee
     * @Description: 字符流转换为整形
     * @Date: 2023/3/26 0:45
     * @Return: 转换结果
     **/
    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result += (bytes[i] & 0xff) << (8 * i);
        }
        return result;
    }
}
