package LSB.web.Function;

import java.awt.*;
import java.awt.image.BufferedImage;
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
     * @Description: 二值图像嵌入
     * @Date: 2023/3/8 17:52
     * @Return: void
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
     * @Description: RGB图像根据阈值进行二值图像转换
     * @Date: 2023/3/8 17:51
     * @Return: void
     **/
    private void trans() {
        try {
            int height = image.getHeight(), width = image.getWidth();
            int[][] color = new int[width][height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    color[j][i] = RGBtoGray(image.getRGB(j, i));
                }
            }
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (analyze_Sobel(color, j, i, width, height) > threshold) {
                        bin.setRGB(j, i, new Color(255, 255, 255).getRGB());
                    } else {
                        bin.setRGB(j, i, new Color(0, 0, 0).getRGB());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: 获取平均灰度值
     * @Date: 2023/3/8 17:49
     * @Return: 灰度值
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

    private double analyze_Sobel(int[][] group, int x, int y, int w, int h) {
        int[][] gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] gy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        int n = 0, m = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                int val = x + j >= w || y + k >= h ? 0 : group[x + j][y + k];
                n += val * gx[j][k];
                m += val * gy[j][k];
            }
        }
        return Math.sqrt(n * n + m * m);
    }

    /**
     * @Author: Nick Lee
     * @Description: 最低位提取
     * @Date: 2023/3/8 17:50
     * @Return: void
     **/
    private void getLast() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: RGB转换为灰度值
     * @Date: 2023/3/26 0:56
     * @Return: 灰度值
     **/
    private int RGBtoGray(int rgb) {
        int blue = getColors(rgb, "B"),
                green = getColors(rgb, "G"),
                red = getColors(rgb, "R");
        return (int) (0.3 * red + 0.59 * green + 0.11 * blue);
    }

    /**
     * @Author: Nick Lee
     * @Description: 获取二值图像的数据流
     * @Date: 2023/3/26 0:57
     * @Return: 数据流
     **/
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

    /**
     * @Author: Nick Lee
     * @Description: 从数据流中获取二值图像
     * @Date: 2023/3/26 0:57
     * @Return: void
     **/
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
     * @Description: 获取二值图像
     * @Date: 2023/3/8 17:50
     * @Return: void
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
     * @Description: 将二值图像直接隐藏于载体中
     * @Date: 2023/3/8 17:51
     * @Return: void
     **/
    public void setImageBin(String read1, String read2, String write) {
        setImage(readPic(read1));
        setBin(readPic(read2));
        setContent();
        writePic(image, write);
    }

    /**
     * @Author: Nick Lee
     * @Description: 从载体获取二值图像
     * @Date: 2023/3/8 17:51
     * @Return: void
     **/
    public void getBinFromImage(String read1, String write) {
        setImage(readPic(read1));
        setBin(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY));
        getLast();
        writePic(bin, write);
    }

    /**
     * @Author: Nick Lee
     * @Description: 将二值图像采用LSB隐藏于载体中
     * @Date: 2023/3/26 0:59
     * @Return: void
     **/
    public void LSB(String key, String read1, String read2, String write, String type, boolean random) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);
        if (random)
            color.rand_getColor(key);
        else
            color.norm_getColor();
        if (type.equals("encrypt")) {
            if (random)
                color.rand_setContent(get_bin_Binary(key, read2), key);
            else
                color.setContent(get_bin_Binary(key, read2));
            color.setImage();
            writePic(image, write);
        } else {
            set_bin_Binary(color.getContent(), key, write);
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: 将二值图像采用MLSB隐藏于载体中
     * @Date: 2023/3/26 0:59
     * @Return: void
     **/
    public void MLSB(String key, String read1, String read2, String write, String type, boolean random) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);
        if (random)
            color.rand_getColor(key);
        else
            color.norm_getColor();
        if (type.equals("encrypt")) {
            color.mLSB_setImage(get_bin_Binary(key, read2), key, random);
            writePic(image, write);
        } else {
            set_bin_Binary(color.getContent(), key, write);
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: 将二值图像采用差分嵌入隐藏于载体中
     * @Date: 2023/3/26 0:59
     * @Return: void
     **/
    public void Diff(String key, String read1, String read2, String write, String type, boolean random) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);
        color.norm_getColor();
        if (type.equals("encrypt")) {
            color.setDiff_Content(get_bin_Binary(key, read2), key, random);
            writePic(image, write);
        } else {
            set_bin_Binary(color.getDiff(key, random), key, write);
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: 获取灰度图
     * @Date: 2023/3/26 1:00
     * @Return: void
     **/
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
