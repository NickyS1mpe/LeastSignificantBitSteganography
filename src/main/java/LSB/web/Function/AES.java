package LSB.web.Function;

import LSB.web.Controller.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @ClassName: AES
 * @Description: aes decrypt and encrypt
 * @Author: Nick Lee
 * @Date: Create in 18:03 2023/3/11
 **/
public class AES {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

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
     * @Description: MD5
     * @Date: 2023/2/28 23:31
     * @Return:
     **/
    public String Encrypt_MD5(String mes, String key) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] ms = messageDigest.digest((mes + key).getBytes());
            return new String(ms, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
