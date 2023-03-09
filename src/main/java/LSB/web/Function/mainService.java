package LSB.web.Function;

import LSB.web.Function.Decrypt;
import LSB.web.Function.Encrypt;
import LSB.web.Function.LSB_Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    Encrypt encrypt;
    Decrypt decrypt;

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
     * @Description: read txt docs
     * @Date: 2023/3/7 22:41
     * @Return:
     **/
    public boolean readTXT(String road) {
        try {
            byte[] date = Files.readAllBytes(Paths.get(road));
            mes = new String(date, StandardCharsets.UTF_8);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Author: Nick Lee
     * @Description: write txt docs
     * @Date: 2023/3/7 22:41
     * @Return:
     **/
    public boolean writeTXT(String road) {
        try {
            File file = new File(road);
            if (file.createNewFile()) {
                BufferedWriter bf = new BufferedWriter(new FileWriter(file));
                bf.write(mes);
                bf.flush();
            }
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
     * @Description: set picture color
     * @Date: 2023/3/7 22:39
     * @Return:
     **/
    public void set() {
        color.setContent(encrypt.mes.getBinary());
        color.setImage();
    }

    /**
     * @Author: Nick Lee
     * @Description: decrypt message
     * @Date: 2023/2/27 9:48
     * @Return:
     **/
    public boolean Decrypt(String key) {
        try {
            decrypt = new Decrypt(color.getContent(), key);
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
    public boolean Encrypt(String key) {
        try {
            encrypt = new Encrypt(mes, key);
            encrypt.Encode();
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
    public void LSB_Control(String sel, String key, String read, String write, String readTXT) {
        if (readPic(read)) {
            load();
            if (sel.equals("r")) {
                Decrypt(key);
                System.out.println(mes);
            } else if (sel.equals("w")) {
                readTXT(readTXT);
                Encrypt(key);
                set();
                if (writePic(write))
                    System.out.println("Encrypt Success");
            } else
                System.out.println("Wrong Input");
        } else
            System.out.println("Loading error");
    }
}
