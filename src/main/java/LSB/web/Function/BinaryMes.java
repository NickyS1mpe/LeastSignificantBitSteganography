package LSB.web.Function;

import org.springframework.util.Base64Utils;

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
public class BinaryMes extends IO {

    BufferedImage image;
    LSB_Image color;
    String mes;
    StrToBin encrypt;
    BinToStr decrypt;


    /**
     * @Author: Nick Lee
     * @Description: 读取txt文件
     * @Date: 2023/3/7 22:41
     * @Return: void
     **/
    public void readTXT(String road) {
        try {
            byte[] date = Files.readAllBytes(Paths.get(road));
            mes = new String(date, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: 写txt文件
     * @Date: 2023/3/7 22:41
     * @Return: void
     **/
    public void writeTXT(String road) {
        try {
            File file = new File(road);
            if (file.createNewFile()) {
                BufferedWriter bf = new BufferedWriter(new FileWriter(file));
                bf.write(mes);
                bf.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: 加载图像，选择是否存在随机量
     * @Date: 2023/2/27 9:48
     * @Return: void
     **/
    public void load(String key, boolean random) {
        color = new LSB_Image(image);
        if (random)
            color.rand_getColor(key);
        else
            color.norm_getColor();
    }

    /**
     * @Author: Nick Lee
     * @Description: LSB，选择是否存在随机量
     * @Date: 2023/3/7 22:39
     * @Return: void
     **/
    public void set(String key, boolean random) {
        if (random) {
            color.rand_setContent(encrypt.getBinary(), key);
        } else {
            color.setContent(encrypt.getBinary());
        }
        color.setImage();
    }

    /**
     * @Author: Nick Lee
     * @Description: LSB匹配，选择是否存在随机量
     * @Date: 2023/3/26 1:10
     * @Return: void
     **/
    public void set_mLSB(String key, boolean random) {
        color.mLSB_setImage(encrypt.getBinary(), key, random);
    }

    /**
     * @Author: Nick Lee
     * @Description: 差分加密，选择是否存在随机量
     * @Date: 2023/3/12 1:12
     * @Return: void
     **/
    public void set_Diff(String key, boolean random) {
        color.setDiff_Content(encrypt.getBinary(), key, random);
    }

    /**
     * @Author: Nick Lee
     * @Description: 差分解密，选择是否存在随机量
     * @Date: 2023/3/12 1:12
     * @Return:
     **/
    public void De_Diff(String key, boolean AES, boolean random) {
        try {
            decrypt = new BinToStr(color.getDiff(key, random));
            decrypt.Decode();
            mes = decrypt.getMes();
            if (AES) {
                mes = mes.substring(24, 24 + Integer.parseInt(Decrypt_AES(mes.substring(0, 24), key).trim(), 16));
                mes = decode_Base64(Decrypt_AES(mes, key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: LSB解密
     * @Date: 2023/2/27 9:48
     * @Return: void
     **/
    public void Decrypt(String key, boolean AES) {
        try {
            decrypt = new BinToStr(color.getContent());
            decrypt.Decode();
            mes = decrypt.getMes();
            if (AES) {
                mes = mes.substring(24, 24 + Integer.parseInt(Decrypt_AES(mes.substring(0, 24), key).trim(), 16));
                mes = decode_Base64(Decrypt_AES(mes, key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Decrypt(int[] binary, String key, boolean AES) {
        try {
            decrypt = new BinToStr(binary);
            decrypt.Decode();
            mes = decrypt.getMes();
            if (AES) {
                mes = mes.substring(24, 24 + Integer.parseInt(Decrypt_AES(mes.substring(0, 24), key).trim(), 16));
                mes = decode_Base64(Decrypt_AES(mes, key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: encrypt message
     * @Date: 2023/2/27 9:49
     * @Return:
     **/
    public boolean Encrypt(String key, boolean AES) {
        try {
            String base64 = encode_Base64(mes);
            if (AES) {
                base64 = Encrypt_AES(base64, key);
                String hex = Encrypt_AES(toHEX(base64.length()), key);
                mes = hex + base64;
            }
             encrypt = new StrToBin(mes);
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

    public int[] getBin() {
        return encrypt.getBinary();
    }

    /**
     * @Author: Nick Lee
     * @Description: base64 encode
     * @Date: 2023/3/1 9:37
     * @Return:
     **/
    private String encode_Base64(String s) {
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
    private String toHEX(int s) {
        StringBuilder sb = new StringBuilder();
        String hex = Integer.toHexString(s);
        sb.append(" ".repeat(8 - hex.length()));
        return sb + hex;
    }

    /**
     * @Author: Nick Lee
     * @Description: base64 decode
     * @Date: 2023/3/1 9:37
     * @Return:
     **/
    private String decode_Base64(String s) {
        try {
            byte[] bt = Base64Utils.decodeFromString(s);
            s = new String(bt, StandardCharsets.UTF_8);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
