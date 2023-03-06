package LSB.web.Function;

import LSB.web.Function.Decrypt;
import LSB.web.Function.Encrypt;
import LSB.web.Function.LSB_Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @ClassName: main
 * @Description: main service
 * @Author: Nick Lee
 * @Date: Create in 15:46 2023/2/22
 **/
public class mainService {

    BufferedImage image;
    LSB_Color color;
    String mes;

    /**
     * @Author: Nick Lee
     * @Description: read picture
     * @Date: 2023/2/27 9:49
     * @Return:
     **/
    public boolean readPic(String road) {
        try {
            image = ImageIO.read(new File(road));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Author: Nick Lee
     * @Description: write picture
     * @Date: 2023/2/27 9:48
     * @Return:
     **/
    public boolean writePic(String road) {
        try {
            String format = road.substring(road.lastIndexOf(".") + 1);
            ImageIO.write(color.getImage(), format, new File(road));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Author: Nick Lee
     * @Description: load class color
     * @Date: 2023/2/27 9:48
     * @Return:
     **/
    public void load() {
        color = new LSB_Color(image);
        color.getColor();
    }

    /**
     * @Author: Nick Lee
     * @Description: decrypt message
     * @Date: 2023/2/27 9:48
     * @Return:
     **/
    public boolean Decrypt(String key) {
        try {
            Decrypt decrypt = new Decrypt(color.getContent(), key);
            decrypt.Decode();
            mes = decrypt.mes.getMes();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Author: Nick Lee
     * @Description: encrypt message
     * @Date: 2023/2/27 9:49
     * @Return:
     **/
    public boolean Encrypt(String message,String key) {
        try {
            mes = message;
            Encrypt encrypt = new Encrypt(mes, key);
            encrypt.Encode();
            color.setContent(encrypt.mes.getBinary());
            color.setImage();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Author: Nick Lee
     * @Description: main controller
     * @Date: 2023/2/27 9:49
     * @Return:
     **/
    public void LSB_Control(String sel, String cur, String key, String read, String write) {
        if (readPic(read)) {
            load();
            if (sel.equals("r")) {
                Decrypt(key);
                System.out.println(mes);
            } else if (sel.equals("w")) {
                Encrypt(cur,key);
                if (writePic(write))
                    System.out.println("Encrypt Success");
            } else
                System.out.println("Wrong Input");
        } else
            System.out.println("Loading error");
    }

}
