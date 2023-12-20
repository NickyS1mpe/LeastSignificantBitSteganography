import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

//图像处理程序
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

    //二值图像的嵌入
    //默认嵌入B通道
    private void setContent() {
        int height = bin.getHeight(), width = bin.getWidth();
        int h = image.getHeight(), w = image.getWidth(), hr = 0, wr = 0;
        int[][] content = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                content[j][i] = bin.getRGB(j, i) & 0x1;
            }
        }

        //加入随机偏移量，使嵌入位置不固定
        Random r = new Random();
        if (h > height && w > width) {
            hr = r.nextInt(h - height - 1);
            wr = r.nextInt(w - width - 1);
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

    //二值图像转换
    //采用Sobel边缘检测算法
    //采用单一阈值，超过阈值则将该像素置为白色，反之为黑色
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

    //获取像素的平均灰度值
    //超出范围的像素值置为255
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

    //Sobel边缘检测算法
    private double analyze_Sobel(int[][] group, int x, int y, int w, int h) {
        //Sobel算子
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

    //提取B通道最低位
    //用于直接嵌入的二值图像的提取
    //像素值最低位为1则将对应的像素点置为白色，反之置为黑色
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

    //RGB转换为灰度值
    //按比例进行转换
    private int RGBtoGray(int rgb) {
        int blue = getColors(rgb, "B"),
                green = getColors(rgb, "G"),
                red = getColors(rgb, "R");
        return (int) (0.3 * red + 0.59 * green + 0.11 * blue);
    }

    //将二值图像转换为数据流
    //将二值图像的长和宽进行AES加密后附于数据流的前端
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

    //将数据流转化为二值图像
    //从数据流的前端提取二值图像的长和宽进行AES解密用于生成二值图像
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

    //十进制数转换为8位长的十六进制字符串
    private String toHEX(int s) {
        StringBuilder sb = new StringBuilder();
        String hex = Integer.toHexString(s);
        sb.append(" ".repeat(8 - hex.length()));
        return sb + hex;
    }

    //获取二值图像的函数
    public void getBinPic(int threshold, String read, String write) {
        setImage(readPic(read));
        setBin(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY));
        setThreshold(threshold);
        trans();
        writePic(bin, write);
    }

    //对二值图像进行直接嵌入的函数
    public void setImageBin(String read1, String read2, String write) {
        setImage(readPic(read1));
        setBin(readPic(read2));
        setContent();
        writePic(image, write);
    }

    //对采用直接嵌入的二值图像进行提取
    public void getBinFromImage(String read1, String write) {
        setImage(readPic(read1));
        setBin(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY));
        getLast();
        writePic(bin, write);
    }

    //采用LSB替换嵌入对二值图像进行嵌入的函数
    public void LSB(String key, String read1, String read2, String write, String type, boolean random) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);

        //是否采用伪随机序列
        if (random)
            color.rand_getColor(key);
        else
            color.norm_getColor();

        //进行加密或解密处理
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

    //采用LSB匹配嵌入对二值图像进行嵌入的函数
    public void MLSB(String key, String read1, String read2, String write, String type, boolean random) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);

        //是否采用伪随机序列
        if (random)
            color.rand_getColor(key);
        else
            color.norm_getColor();

        //进行加密或解密处理
        if (type.equals("encrypt")) {
            color.mLSB_setImage(get_bin_Binary(key, read2), key, random);
            writePic(image, write);
        } else {
            set_bin_Binary(color.getContent(), key, write);
        }
    }

    //采用LSB差分嵌入对二值图像进行嵌入的函数
    public void Diff(String key, String read1, String read2, String write, String type, boolean random) {
        setImage(readPic(read1));
        LSB_Image color = new LSB_Image(image);
        color.norm_getColor();

        //进行加密或解密处理
        if (type.equals("encrypt")) {
            color.setDiff_Content(get_bin_Binary(key, read2), key, random);
            writePic(image, write);
        } else {
            set_bin_Binary(color.getDiff(key, random), key, write);
        }
    }

    //获取图像灰度图的函数
    public void getGrayPic(int layer, int type, String read1, String write) {
        setImage(readPic(read1));
        setBin(new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
        int[] dat = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
        int h = image.getHeight(), w = image.getWidth();

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                //获取图像灰度值
                int gray = RGBtoGray(image.getRGB(j, i));

                //去除图像其他位保留某一位
                if (type == 1) {
                    gray &= dat[layer];
                }

                //去除图像某一位保留其他位
                else if (type == 2) {
                    gray &= 255 - dat[layer];
                }
                bin.setRGB(j, i, colorToRGB(255, gray, gray, gray));
            }
        }
        writePic(bin, write);
    }

    //将Alpha、R、G、B值转换为RGB值
    private int colorToRGB(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    //增加椒盐噪声的函数
    public void addNoise(double noiseProbability, String read, String write) {
        setImage(readPic(read));
        BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Random r = new Random();
        String[] cor = {"R", "G", "B"};

        for (int i = 0; i < image.getHeight(); i++) {
            for (int k = 0; k < image.getWidth(); k++) {
                int rgb = image.getRGB(k, i);
                for (int j = 0; j < 3; j++) {
                    //添加噪声
                    if (r.nextDouble() < noiseProbability) {
                        int red = getColors(rgb, cor[0]), green = getColors(rgb, cor[1]), blue = getColors(rgb, cor[2]);
                        int noise = r.nextInt(256);
                        red = (noise & 0xfe) | (red & 0x01);
                        green = (noise & 0xfe) | (green & 0x01);
                        blue = (noise & 0xfe) | (blue & 0x01);
                        rgb = colorToRGB(255, red, green, blue);
                    }
                }
                im.setRGB(k, i, rgb);
            }
        }
        writePic(im, write);
    }
}
