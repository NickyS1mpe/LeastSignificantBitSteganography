package LSB.web.Model;

import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @ClassName: Decode
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 15:46 2023/2/22
 **/
public class Decrypt {
    String mes;
    int[] dec;

    public Decrypt(int[] dec) {
        this.dec = dec;
    }

    public void Decode() {
        try {
            StringBuilder sb = new StringBuilder();
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
            mes = sb.toString().trim();
            mes = decode_Base64(mes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMes() {
        return mes;
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
