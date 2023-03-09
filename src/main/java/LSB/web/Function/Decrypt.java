package LSB.web.Function;

import LSB.web.Model.Mes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @ClassName: Decode
 * @Description: decrypt
 * @Author: Nick Lee
 * @Date: Create in 15:46 2023/2/22
 **/
public class Decrypt {
    Mes mes;

    public Decrypt(int[] dec, String key) {
        mes = new Mes();
        mes.setKey(key);
        mes.setBinary(dec);
    }

    public void Decode() {
        try {
            StringBuilder sb = new StringBuilder();
            int[] dec = mes.getBinary();
            for (int i = 0; i < dec.length / 8; i++) {
                char c = 0;
                if (dec[8 * i] == 1) c |= 0x1;
                if (dec[8 * i + 1] == 1) c |= 0x2;
                if (dec[8 * i + 2] == 1) c |= 0x4;
                if (dec[8 * i + 3] == 1) c |= 0x8;
                if (dec[8 * i + 4] == 1) c |= 0x10;
                if (dec[8 * i + 5] == 1) c |= 0x20;
                if (dec[8 * i + 6] == 1) c |= 0x40;
                if (dec[8 * i + 7] == 1) c |= 0x80;
                sb.append(c);
            }
            String message = sb.substring(24, 24 + Integer.parseInt(Decrypt_AES(sb.substring(0, 24), mes.getKey()).trim(), 16));
            message = Decrypt_AES(message, mes.getKey());
            mes.setMes(decode_Base64(message));
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
    public String Decrypt_AES(String mes, String key) {
        if (key == null || mes == null)
            return "decrypt error";
        if (key.length() != 16)
            return "the length of key is not 16";
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
     * @Description: base64 decode
     * @Date: 2023/3/1 9:37
     * @Return:
     **/
    public String decode_Base64(String s) {
        try {
            byte[] bt = Base64Utils.decodeFromString(s);
            s = new String(bt, StandardCharsets.UTF_8);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
