package LSB.web.Function;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @ClassName: LSB_Color
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 16:47 2023/2/24
 **/
public class LSB_Image extends IO {
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
    public void norm_getColor() {
        try {
            int height = image.getHeight(), width = image.getWidth();
            content = new int[height * width * 3];
            for (int i = 0; i < height; i++) {
                for (int k = 0; k < width; k++) {
                    var rgb = image.getRGB(k, i);
                    content[(i * width + k) * 3] = getColors(rgb, "R") & 0x1;
                    content[(i * width + k) * 3 + 1] = getColors(rgb, "G") & 0x1;
                    content[(i * width + k) * 3 + 2] = getColors(rgb, "B") & 0x1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Author: Nick Lee
     * @Description: normal
     * @Date: 2023/3/11 18:30
     * @Return:
     **/
    public void setContent(int[] binary) {
        try {
            System.out.println("嵌入率为" + binary.length * 1.0 / content.length);
            System.arraycopy(binary, 0, content, 0, binary.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: random
     * @Date: 2023/3/11 18:31
     * @Return:
     **/
    public void rand_getColor(String key) {
        try {
            int height = image.getHeight(), width = image.getWidth();
            content = new int[height * width * 3];
            var list = generateRandomList(content.length, key);
            int count = 0;
            String[] cor = {"R", "G", "B"};
            for (int i = 0; i < height; i++) {
                for (int k = 0; k < width; k++) {
                    int rgb = image.getRGB(k, i), tmp = (i * width + k) * 3;
                    for (int x = 0; x < 3; x++) {
                        if (list.get(tmp + x) == 1) {
                            content[count++] = getColors(rgb, cor[x]) & 0x1;
                        }
                    }
                }
            }
            for (int i = 0; i < height; i++) {
                for (int k = 0; k < width; k++) {
                    int rgb = image.getRGB(k, i), tmp = (i * width + k) * 3;
                    for (int x = 0; x < 3; x++) {
                        if (list.get(tmp + x) == 0) {
                            content[count++] = getColors(rgb, cor[x]) & 0x1;
                        }
                    }
                }
            }
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
    public void rand_setContent(int[] binary, String key) {
        try {
            System.out.println("嵌入率为" + binary.length * 1.0 / content.length);
            var list = generateRandomList(content.length, key);
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == 1)
                    content[i] = binary[count++];
                if (count >= binary.length)
                    break;
            }
            if (count < binary.length) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == 0)
                        content[i] = binary[count++];
                    if (count >= binary.length)
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3;
                var blue = getColors(rgb, "B");
                var green = getColors(rgb, "G");
                var red = getColors(rgb, "R");
                red = content[st] == 1 ? red | 0x1 : red & 0xFE;
                green = content[st + 1] == 1 ? green | 0x1 : green & 0xFE;
                blue = content[st + 2] == 1 ? blue | 0x1 : blue & 0xFE;
                rgb = (rgb >> 24) << 24 | red << 16 | green << 8 | blue;
                image.setRGB(k, i, rgb);
            }
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: M_LSB
     * @Date: 2023/3/11 21:01
     * @Return:
     **/
    public void mLSB_setImage(int[] binary, String key, boolean random) {
        int height = image.getHeight(), width = image.getWidth();
        int[] bigBin;
        if (random) {
            bigBin = new int[height * width * 3];
            rand_setContent(binary, key);
            System.arraycopy(content, 0, bigBin, 0, content.length);
        } else {
            bigBin = new int[(binary.length / 3 + 1) * 3];
            System.arraycopy(binary, 0, bigBin, 0, binary.length);
        }
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3;
                if (st >= bigBin.length)
                    break;
                int blue = getColors(rgb, "B");
                int green = getColors(rgb, "G");
                int red = getColors(rgb, "R");
                rgb = (rgb >> 24) << 24 | setColor(bigBin[st], red) << 16 |
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

    public void setDiff_Content(int[] binary, String key, boolean random) {
        int height = image.getHeight(), width = image.getWidth();
        int[] bigBin;
        if (random) {
            bigBin = new int[height * width * 3];
            int[] diff = getDiff(null, false);
            var list = generateRandomList(diff.length, key);
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == 1)
                    diff[i] = binary[count++];
                if (count >= binary.length)
                    break;
            }
            if (count < binary.length) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == 0)
                        diff[i] = binary[count++];
                    if (count >= binary.length)
                        break;
                }
            }
            System.out.println("嵌入率为" + binary.length * 1.0 / content.length);
            System.arraycopy(diff, 0, bigBin, 0, diff.length);
        } else {
            bigBin = new int[(binary.length / 3 + 1) * 3];
            System.arraycopy(binary, 0, bigBin, 0, binary.length);
        }
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3, f_blue;
                if (st >= bigBin.length)
                    break;
                int blue = getColors(rgb, "B");
                int green = getColors(rgb, "G");
                int red = getColors(rgb, "R");
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

    public int[] getDiff(String key, boolean random) {
        diff = new int[content.length - 1];
        for (int i = 0; i < content.length - 1; i++) {
            if (i == 0)
                diff[i] = content[i];
            else
                diff[i] = Math.abs(content[i] - content[i - 1]);
        }
        if (random) {
            var list = generateRandomList(diff.length, key);
            int count = 0;
            int[] bin = new int[diff.length];
            for (int i = 0; i < diff.length; i++) {
                if (list.get(i) == 1)
                    bin[count++] = diff[i];
            }
            for (int i = 0; i < diff.length; i++) {
                if (list.get(i) == 0)
                    bin[count++] = diff[i];
            }
            return bin;
        }
        return diff;
    }

    public int[] getContent() {
        return content;
    }
}
