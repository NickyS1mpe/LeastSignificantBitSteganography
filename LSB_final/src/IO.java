import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

//工具类程序
public class IO extends Crypto {

    //图像读取
    //读取结果返回BufferedImage
    public BufferedImage readPic(String road) {
        try {
            return ImageIO.read(new File(road));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //图像写入
    public void writePic(BufferedImage image, String road) {
        try {
            String format = road.substring(road.lastIndexOf(".") + 1);
            ImageIO.write(image, format, new File(road));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取RGB值中的指定通道的值
    //target输入字符值R、G、B对应三个通道
    public int getColors(int rgb, String target) {
        int blue = rgb - ((rgb >> 8) << 8);
        rgb >>= 8;
        int green = rgb - ((rgb >> 8) << 8);
        rgb >>= 8;
        int red = rgb - ((rgb >> 8) << 8);

        switch (target) {
            case "R":
                return red;
            case "G":
                return green;
            case "B":
                return blue;
        }
        return 0;
    }

    //将指定通道的值置于RGB值中
    //target输入字符值R、G、B对应三个通道
    public int setRGB(int rgb, int cur, String target) {
        var blue = getColors(rgb, "B");
        var green = getColors(rgb, "G");
        var red = getColors(rgb, "R");

        switch (target) {
            case "R":
                return (rgb >> 24) << 24 |
                        cur << 16 |
                        green << 8 |
                        blue;
            case "G":
                return (rgb >> 24) << 24 |
                        red << 16 |
                        cur << 8 |
                        blue;
            case "B":
                return (rgb >> 24) << 24 |
                        red << 16 |
                        green << 8 |
                        cur;
        }
        return 0;
    }

    //新建文件夹
    public void createFolder(String road) {
        File folder = new File(road);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.setWritable(true, false);
            folder.mkdirs();
        }
    }
}
