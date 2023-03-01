package LSB.web.Function;

import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @ClassName: Encode
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 15:46 2023/2/22
 **/
public class Encrypt {
    String mes;
    int[] binary;
    public final static String key = "passwd";

    public Encrypt(String mes) {
        this.mes = mes;
    }

    public void Encode() {
        mes = encode_Base64(mes);
        binary = new int[mes.length() * 8];
        try {
            for (int i = 0; i < mes.length(); i++) {
                binary[8 * i] = mes.charAt(i) & 0x1;
                binary[8 * i + 1] = mes.charAt(i) >> 1 & 0x1;
                binary[8 * i + 2] = mes.charAt(i) >> 2 & 0x1;
                binary[8 * i + 3] = mes.charAt(i) >> 3 & 0x1;
                binary[8 * i + 4] = mes.charAt(i) >> 4 & 0x1;
                binary[8 * i + 5] = mes.charAt(i) >> 5 & 0x1;
                binary[8 * i + 6] = mes.charAt(i) >> 6 & 0x1;
                binary[8 * i + 7] = mes.charAt(i) >> 7 & 0x1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getBinary() {
        return binary;
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
            byte[] ms = messageDigest.digest((mes + key).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
