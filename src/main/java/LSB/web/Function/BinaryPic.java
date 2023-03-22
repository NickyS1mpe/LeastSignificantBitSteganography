package LSB.web.Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @ClassName: BinaryPic
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 14:59 2023/3/8
 **/
public class BinaryPic extends IO {
    BufferedImage image, bin;
    int threshold;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getBin() {
        return bin;
    }

    public void setBin(BufferedImage bin) {
        this.bin = bin;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * @Author: Nick Lee
     * @Description: change the last bit of an image to put in a binary image
     * @Date: 2023/3/8 17:52
     * @Return:
     **/
    private void setContent() {
        int height = bin.getHeight(), width = bin.getWidth();
        int h = image.getHeight(), w = image.getWidth(), hr = 0, wr = 0;
        int[][] content = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                content[j][i] = bin.getRGB(j, i) & 0x1;
            }
        }
        Random r = new Random();
        if (h > height && w > width) {
            hr = r.nextInt(h - height - 1);
            wr = r.nextInt(w - width - 1);//加入随机量，使嵌入位置不固定
        }
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (i < height && j < width) {
                    int rgb = image.getRGB(j + wr, i + hr);
                    rgb = content[j][i] == 1 ? rgb | 0x00000001 : rgb & 0XFFFFFFFE;
                    image.setRGB(j + hr, i + wr, rgb);
                }
            }
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: set binary image
     * @Date: 2023/3/8 17:51
     * @Return:
     **/
    private boolean trans() {
        try {
            int height = image.getHeight(), width = image.getWidth();
            int[][] color = new int[width][height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color c = new Color(image.getRGB(j, i));
                    int r = c.getRed(), b = c.getBlue(), g = c.getGreen();
                    color[j][i] = (r + g + b) / 3;
                }
            }
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (getGray(color, j, i, width, height) > threshold) {
                        bin.setRGB(j, i, new Color(255, 255, 255).getRGB());
                    } else {
                        bin.setRGB(j, i, new Color(0, 0, 0).getRGB());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Author: Nick Lee
     * @Description: get average degree of grayness
     * @Date: 2023/3/8 17:49
     * @Return:
     **/
    private static int getGray(int[][] gray, int x, int y, int w, int h) {
        int rs = gray[x][y]
                + (x == 0 ? 255 : gray[x - 1][y])
                + (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])
                + (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])
                + (y == 0 ? 255 : gray[x][y - 1])
                + (y == h - 1 ? 255 : gray[x][y + 1])
                + (x == w - 1 ? 255 : gray[x + 1][y])
                + (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])
                + (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);
        return rs / 9;
    }

    /**
     * @Author: Nick Lee
     * @Description: get the last bit of image
     * @Date: 2023/3/8 17:50
     * @Return:
     **/
    private boolean getLast() {
        try {
            int height = image.getHeight(), width = image.getWidth();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if ((image.getRGB(j, i) & 0x1) == 1) {
                        bin.setRGB(j, i, new Color(255, 255, 255).getRGB());
                    } else {
                        bin.setRGB(j, i, new Color(0, 0, 0).getRGB());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int RGBtoGray(int rgb) {
        int blue = getColors(rgb, "B"),
                green = getColors(rgb, "G"),
                red = getColors(rgb, "R");
        return (int) (0.3 * red + 0.59 * green + 0.11 * blue);
    }

    public int[] get_bin_Binary(String key, String road) {
        setBin(readPic(road));
        int h = bin.getHeight(), w = bin.getWidth();
        int[] res = new int[48 * 8 + h * w];
        String head = Encrypt_AES(toHEX(h), key) + Encrypt_AES(toHEX(w), key);
        for (int i = 0; i < 48; i++) {
            for (int j = 0; j < 8; j++) {
                res[8 * i + j] = head.charAt(i) >> j & 0x1;
            }
        }
        for (int i = 0; i < h; i++) {
            for (int k = 0; k < w; k++) {
                res[48 * 8 + i * w + k] = bin.getRGB(k, i) & 0x1;
            }
        }
        return res;
    }

    public void set_bin_Binary(int[] binary, String key, String road) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 48 * 8; i++) {
            char c = 0;
            if (binary[8 * i] == 1) c |= 0x1;
            if (binary[8 * i + 1] == 1) c |= 0x2;
            if (binary[8 * i + 2] == 1) c |= 0x4;
            if (binary[8 * i + 3] == 1) c |= 0x8;
            if (binary[8 * i + 4] == 1) c |= 0x10;
            if (binary[8 * i + 5] == 1) c |= 0x20;
            if (binary[8 * i + 6] == 1) c |= 0x40;
            if (binary[8 * i + 7] == 1) c |= 0x80;
            sb.append(c);
        }
        int h = Integer.parseInt(Decrypt_AES(sb.substring(0, 24), key).trim(), 16),
                w = Integer.parseInt(Decrypt_AES(sb.substring(24, 48), key).trim(), 16);
        setBin(new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY));
        for (int i = 0; i < h; i++) {
            for (int k = 0; k < w; k++) {
                if (binary[48 * 8 + i * w + k] == 1) {
                    bin.setRGB(k, i, new Color(255, 255, 255).getRGB());
                } else {
                    bin.setRGB(k, i, new Color(0, 0, 0).getRGB());
                }
            }
        }
        writePic(bin, road);
    }

    private String toHEX(int s) {
        StringBuilder sb = new StringBuilder();
        String hex = Integer.toHexString(s);
        sb.append(" ".repeat(8 - hex.length()));
        return sb + hex;
    }

    /**
     * @Author: Nick Lee
     * @Description: transform to a binary image
     * @Date: 2023/3/8 17:50
     * @Return:
     **/
    public void getBinPic(int threshold, String read, String write) {
        setImage(readPic(read));
        setBin(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY));
        setThreshold(threshold);
        trans();
        writePic(bin, write);
    }

    /**
     * @Author: Nick Lee
     * @Description: put a binary image into another image
     * @Date: 2023/3/8 17:51
     * @Return:
     **/
    public void setImageBin(String read1, String read2, String write) {
        setImage(readPic(read1));
        setBin(readPic(read2));
        setContent();
        writePic(image, write);
    }

    /**
     * @Author: Nick Lee
     * @Description: get a binary image form another image
     * @Date: 2023/3/8 17:51
     * @Return:
     **/
    public void getBinFromImage(String read1, String write) {
        setImage(readPic(read1));
        setBin(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY));
        getLast();
        writePic(bin, write);
    }

    public void LSB(String key, String read1, String read2, String write, String type) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);
        color.norm_getColor();
        if (type.equals("encrypt")) {
            color.setContent(get_bin_Binary(key, read2));
            color.setImage();
            writePic(image, write);
        } else {
            set_bin_Binary(color.getContent(), key, write);
        }
    }

    public void MLSB(String key, String read1, String read2, String write, String type) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);
        color.norm_getColor();
        if (type.equals("encrypt")) {
            color.mLSB_setImage(get_bin_Binary(key, read2));
            writePic(image, write);
        } else {
            set_bin_Binary(color.getContent(), key, write);
        }
    }

    public void Diff(String key, String read1, String read2, String write, String type) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);
        color.norm_getColor();
        if (type.equals("encrypt")) {
            color.setDiff_Content(get_bin_Binary(key, read2));
            writePic(image, write);
        } else {
            set_bin_Binary(color.getDiff(), key, write);
        }
    }

    public void getGrayPic(String read1, String write) {
        setImage(readPic(read1));
        setBin(new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
        int h = image.getHeight(), w = image.getWidth();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int gray = RGBtoGray(image.getRGB(j, i));
                bin.setRGB(j, i, colorToRGB(255, gray, gray, gray));
            }
        }
        writePic(bin, write);
    }

    private int colorToRGB(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }
}
