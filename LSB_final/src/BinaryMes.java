import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

//文本处理程序
public class BinaryMes extends IO {

    BufferedImage image;
    LSB_Image color;
    String mes;
    StrToBin encrypt;
    BinToStr decrypt;


    //读取txt文件，转化位字符串
    public void readTXT(String road) {
        try {
            byte[] date = Files.readAllBytes(Paths.get(road));
            mes = new String(date, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将字符串写入txt文件
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


    //加载图像，获得图像最低位
    //选择是否采用伪随机序列
    public void load(String key, boolean random) {
        color = new LSB_Image(image);

        if (random)
            color.rand_getColor(key);
        else
            color.norm_getColor();
    }

    //LSB替换嵌入
    //选择是否采用伪随机序列
    public void set(String key, boolean random) {
        if (random) {
            color.rand_setContent(encrypt.getBinary(), key);
        } else {
            color.setContent(encrypt.getBinary());
        }
        color.setImage();
    }

    //LSB匹配嵌入
    //选择是否采用伪随机序列
    public void set_mLSB(String key, boolean random) {
        color.mLSB_setImage(encrypt.getBinary(), key, random);
    }

    //LSB差分嵌入
    //选择是否采用伪随机序列
    public void set_Diff(String key, boolean random) {
        color.setDiff_Content(encrypt.getBinary(), key, random);
    }

    //LSB差分嵌入提取
    //选择是否采用伪随机序列
    public void De_Diff(String key, boolean AES, boolean random) {
        try {
            decrypt = new BinToStr(color.getDiff(key, random));
            decrypt.Decode();
            mes = decrypt.getMes();

            //AES解密
            //BASE64解码
            if (AES) {
                mes = mes.substring(24, 24 + Integer.parseInt(Decrypt_AES(mes.substring(0, 24), key).trim(), 16));
                mes = decode_Base64(Decrypt_AES(mes, key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //LSB最低位提取
    //选择是否采用伪随机序列
    public void Decrypt(String key, boolean AES) {
        try {
            decrypt = new BinToStr(color.getContent());
            decrypt.Decode();
            mes = decrypt.getMes();

            //AES解密
            //BASE64解码
            if (AES) {
                mes = mes.substring(24, 24 + Integer.parseInt(Decrypt_AES(mes.substring(0, 24), key).trim(), 16));
                mes = decode_Base64(Decrypt_AES(mes, key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //LSB最低位提取
    //选择是否采用伪随机序列
    //用于边缘分析嵌入时提取
    public void Decrypt(int[] binary, String key, boolean AES) {
        try {
            decrypt = new BinToStr(binary);
            decrypt.Decode();
            mes = decrypt.getMes();

            //AES解密
            //BASE64解码
            if (AES) {
                mes = mes.substring(24, 24 + Integer.parseInt(Decrypt_AES(mes.substring(0, 24), key).trim(), 16));
                mes = decode_Base64(Decrypt_AES(mes, key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //文本信息加密
    //采用AES（EBC电码本模式）填充加密
    //采用BASE64编码，为了支持中文或特殊字符
    //密钥必须为16位长
    public boolean Encrypt(String key, boolean AES) {
        try {
            //BASE64编码
            String base64 = encode_Base64(mes);

            //AES加密
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

    //BASE64编码
    private String encode_Base64(String s) {
        try {
            byte[] bt = s.getBytes(StandardCharsets.UTF_8);
            s = Base64.getEncoder().encodeToString(bt);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //十进制数转换为8为十六进制字符串
    private String toHEX(int s) {
        StringBuilder sb = new StringBuilder();
        String hex = Integer.toHexString(s);
        sb.append(" ".repeat(8 - hex.length()));
        return sb + hex;
    }

    //BASE64解码
    private String decode_Base64(String s) {
        try {
            byte[] bt = Base64.getDecoder().decode(s);
            s = new String(bt, StandardCharsets.UTF_8);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
