package LSB.web.Function;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName: IO
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 17:47 2023/3/8
 **/
public class IO extends AES{

    /**
     * @Author: Nick Lee
     * @Description: read picture
     * @Date: 2023/3/10 21:16
     * @Return:
     **/
    public BufferedImage readPic(String road) {
        try {
            return ImageIO.read(new File(road));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: Nick Lee
     * @Description: write picture
     * @Date: 2023/3/10 21:16
     * @Return:
     **/
    public void writePic(BufferedImage image, String road) {
        try {
            String format = road.substring(road.lastIndexOf(".") + 1);
            ImageIO.write(image, format, new File(road));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public int setRGB(int rgb, int cur, String target) {
        var blue = getColors(rgb, "B");
        var green = getColors(rgb, "G");
        var red = getColors(rgb, "R");
        switch (target) {
            case "R":
                return rgb << 24 | cur << 16 | green << 8 | blue;
            case "G":
                return rgb << 24 | red << 16 | cur << 8 | blue;
            case "B":
                return rgb << 24 | red << 16 | green << 8 | cur;
        }
        return 0;
    }

    public void createFolder(String road) {
        File folder = new File(road);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.setWritable(true, false);
            folder.mkdirs();
        }
    }
}
