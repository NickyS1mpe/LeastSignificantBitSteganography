package LSB.web.Function;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @ClassName: main
 * @Description: main service
 * @Author: Nick Lee
 * @Date: Create in 15:46 2023/2/22
 **/
public class BinaryMes extends IO {

    BufferedImage image;
    LSB_Image color;
    String mes;
    StrToBin encrypt;
    BinToStr decrypt;


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
        color = new LSB_Image(image);
        color.norm_getColor();
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

    public void set_mLSB() {
        color.mLSB_setImage(encrypt.mes.getBinary());
    }

    /**
     * @Author: Nick Lee
     * @Description: difference encrypt
     * @Date: 2023/3/12 1:12
     * @Return:
     **/
    public void set_Diff() {
        color.setDiff_Content(encrypt.mes.getBinary());
    }

    /**
     * @Author: Nick Lee
     * @Description: difference decrypt
     * @Date: 2023/3/12 1:12
     * @Return:
     **/
    public void De_Diff(String key, boolean AES) {
        try {
            decrypt = new BinToStr(color.getDiff(), key, AES);
            decrypt.Decode();
            mes = decrypt.mes.getMes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: decrypt message
     * @Date: 2023/2/27 9:48
     * @Return:
     **/
    public boolean Decrypt(String key, boolean AES) {
        try {
            decrypt = new BinToStr(color.getContent(), key, AES);
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
    public boolean Encrypt(String key, boolean AES) {
        try {
            encrypt = new StrToBin(mes, key, AES);
            encrypt.Encode();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @Author: Nick Lee
     * @Description: main controller
     * @Date: 2023/2/27 9:49
     * @Return:
     **//*
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
    }*/
}
