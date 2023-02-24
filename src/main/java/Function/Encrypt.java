package Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @ClassName: Encode
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 15:46 2023/2/22
 **/
public class Encrypt {
    String mes;
    int[] binary;

    public Encrypt(String mes) {
        this.mes = mes;
    }

    public void Encode() {
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
}
