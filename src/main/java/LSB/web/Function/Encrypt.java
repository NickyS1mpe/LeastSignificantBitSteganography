package LSB.web.Function;

import LSB.web.Model.Mes;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @ClassName: Encode
 * @Description: encrypt
 * @Author: Nick Lee
 * @Date: Create in 15:46 2023/2/22
 **/
public class Encrypt {
    Mes mes;

    public Encrypt(String message, String key) {
        mes = new Mes();
        mes.setMes(message);
        mes.setKey(key);
    }

    public void Encode() {
        String base64 = encode_Base64(mes.getMes());
        base64 = Encrypt_AES(base64, mes.getKey());
        String hex = Encrypt_AES(toHEX(base64.length()), mes.getKey());
        base64 = hex + base64;
        int[] binary = new int[base64.length() * 8];
        try {
            for (int i = 0; i < base64.length(); i++) {
                binary[8 * i] = base64.charAt(i) & 0x1;
                binary[8 * i + 1] = base64.charAt(i) >> 1 & 0x1;
                binary[8 * i + 2] = base64.charAt(i) >> 2 & 0x1;
                binary[8 * i + 3] = base64.charAt(i) >> 3 & 0x1;
                binary[8 * i + 4] = base64.charAt(i) >> 4 & 0x1;
                binary[8 * i + 5] = base64.charAt(i) >> 5 & 0x1;
                binary[8 * i + 6] = base64.charAt(i) >> 6 & 0x1;
                binary[8 * i + 7] = base64.charAt(i) >> 7 & 0x1;
            }
            mes.setBinary(binary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: MD5
     * @Date: 2023/2/28 23:31
     * @Return:
     **/
    public void Encrypt_MD5() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] ms = messageDigest.digest((mes + mes.getKey()).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: aes encrypt
     * @Date: 2023/3/7 1:12
     * @Return:
     **/
    public String Encrypt_AES(String mes, String key) {
        if (key == null || mes == null)
            return "encrypt error";
        if (key.length() != 16)
            return "the length of key is not 16";
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
     * @Description: base64 encode
     * @Date: 2023/3/1 9:37
     * @Return:
     **/
    public String encode_Base64(String s) {
        try {
            byte[] bt = s.getBytes(StandardCharsets.UTF_8);
            s = Base64Utils.encodeToString(bt);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @Author: Nick Lee
     * @Description: dec to hex
     * @Date: 2023/3/7 22:38
     * @Return:
     **/
    public String toHEX(int s) {
        StringBuilder sb = new StringBuilder();
        String hex = Integer.toHexString(s);
        sb.append(" ".repeat(8 - hex.length()));
        return sb + hex;
    }
}
