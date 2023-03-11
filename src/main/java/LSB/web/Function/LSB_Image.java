package LSB.web.Function;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

/**
 * @ClassName: LSB_Color
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 16:47 2023/2/24
 **/
public class LSB_Image {
    BufferedImage image;
    int[] content, diff;

    public LSB_Image(BufferedImage image) {
        this.image = image;
        this.content = new int[image.getHeight() * image.getWidth() * 8];
    }

    /**
     * @Author: Nick Lee
     * @Description: get RGB final bit from image
     * @Date: 2023/3/1 9:39
     * @Return:
     **/
    public int[] norm_getColor() {
        try {
            int height = image.getHeight(), width = image.getWidth();
            content = new int[height * width * 3];
            for (int i = 0; i < height; i++) {
                for (int k = 0; k < width; k++) {
                    Color c = new Color(image.getRGB(k, i));
                    content[(i * width + k) * 3] = c.getRed() & 0x1;
                    content[(i * width + k) * 3 + 1] = c.getGreen() & 0x1;
                    content[(i * width + k) * 3 + 2] = c.getBlue() & 0x1;
                }
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: Nick Lee
     * @Description: random
     * @Date: 2023/3/11 18:31
     * @Return:
     **/
    public int[] rand_getColor(String key) {
        try {
            int height = image.getHeight(), width = image.getWidth();
            content = new int[height * width * 3];

            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: Nick Lee
     * @Description: normal
     * @Date: 2023/3/11 18:30
     * @Return:
     **/
    public void setContent(int[] binary) {
        try {
            System.arraycopy(binary, 0, content, 0, binary.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: random
     * @Date: 2023/3/11 18:30
     * @Return:
     **/
    public void rand_setContent(int[] binary) {
        try {
            System.arraycopy(binary, 0, content, 0, binary.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Author: Nick Lee
     * @Description: RGB transform
     * @Date: 2023/3/1 9:38
     * @Return:
     **/
    public int changeToRGB(int rgb, int st) {
        rgb = content[st] == 1 ? rgb | 0x00010000 : rgb & 0XFFFEFFFF;
        rgb = content[st + 1] == 1 ? rgb | 0x00000100 : rgb & 0XFFFFFEFF;
        rgb = content[st + 2] == 1 ? rgb | 0x00000001 : rgb & 0XFFFFFFFE;
        return rgb;
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * @Author: Nick Lee
     * @Description: set image RGB
     * @Date: 2023/3/1 9:39
     * @Return:
     **/
    public void setImage() {
        int height = image.getHeight(), width = image.getWidth();
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                image.setRGB(k, i, changeToRGB(image.getRGB(k, i), (i * width + k) * 3));
            }
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: M_LSB
     * @Date: 2023/3/11 21:01
     * @Return:
     **/
    public void mLSB_setImage(int[] binary) {
        int height = image.getHeight(), width = image.getWidth();
        int[] bigBin = new int[(binary.length / 3 + 1) * 3];
        System.arraycopy(binary, 0, bigBin, 0, binary.length);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3;
                if (st >= bigBin.length)
                    break;
                int blue = rgb - ((rgb >> 8) << 8);
                rgb >>= 8;
                int green = rgb - ((rgb >> 8) << 8);
                rgb >>= 8;
                int red = rgb - ((rgb >> 8) << 8);
                rgb >>= 8;
                rgb = rgb << 24 | setColor(bigBin[st], red) << 16 |
                        setColor(bigBin[st + 1], green) << 8 |
                        setColor(bigBin[st + 2], blue);
                image.setRGB(k, i, rgb);
            }
        }
    }

    private int setColor(int con, int col) {
        if (con != (col & 0x1)) {
            if (col == 0)
                col++;
            else if (col == 255)
                col--;
            else {
                Random r = new Random();
                col += r.nextInt() % 2 == 1 ? 1 : -1;
            }
        }
        return col;
    }

    public void setDiff_Content(int[] binary) {
        int height = image.getHeight(), width = image.getWidth();
        int[] bigBin = new int[(binary.length / 3 + 1) * 3];
        System.arraycopy(binary, 0, bigBin, 0, binary.length);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3, f_blue;
                if (st >= bigBin.length)
                    break;
                int blue = rgb - ((rgb >> 8) << 8);
                rgb >>= 8;
                int green = rgb - ((rgb >> 8) << 8);
                rgb >>= 8;
                int red = rgb - ((rgb >> 8) << 8);
                rgb >>= 8;
                if (!(i == 0 && k == 0)) {
                    int fi = i, fk = k - 1;
                    if (fk == -1) {
                        fi = i - 1;
                        fk = width - 1;
                    }
                    int f_rgb = image.getRGB(fk, fi);
                    f_blue = f_rgb - ((f_rgb >> 8) << 8);
                } else
                    f_blue = 0;
                red = setDiff(f_blue, red, bigBin[st]);
                green = setDiff(red, green, bigBin[st + 1]);
                blue = setDiff(green, blue, bigBin[st + 2]);
                rgb = rgb << 24 | red << 16 | green << 8 | blue;
                image.setRGB(k, i, rgb);
            }
        }
    }

    private int setDiff(int col1, int col2, int con) {
        Random r = new Random();
        if (Math.abs((col2 & 0x1) - (col1 & 0x1)) != con) {
            if (col2 == 255)
                col2--;
            else if (col2 == 0)
                col2++;
            else
                col2 += r.nextInt() % 2 == 1 ? 1 : -1;
        }
        return col2;
    }

    public int[] getDiff() {
        diff = new int[content.length - 1];
        for (int i = 0; i < content.length - 1; i++) {
            if (i == 0)
                diff[i] = content[i];
            else
                diff[i] = Math.abs(content[i] - content[i - 1]);
        }
        return diff;
    }

    public int[] getContent() {
        return content;
    }
}
