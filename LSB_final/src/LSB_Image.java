import java.awt.image.BufferedImage;
import java.util.Random;

//图像LSB最低位处理程序
public class LSB_Image extends IO {
    BufferedImage image;
    int[] content, diff;

    public LSB_Image(BufferedImage image) {
        this.image = image;
        this.content = new int[image.getHeight() * image.getWidth() * 8];
    }

    //获取图像的LSB最低位
    //按R、G、B三通道顺序读取
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


    //LSB替换嵌入
    //直接将图像的最低位LSB替换为二进制序列
    public void setContent(int[] binary) {
        try {
            System.out.println("嵌入率为" + binary.length * 1.0 / content.length);

            System.arraycopy(binary, 0, content, 0, binary.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取图像的LSB最低位
    //采用伪随机序列进行提取
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
                        if (list.get(tmp + x) == 0 && (tmp + x) % 2 == 1) {
                            content[count++] = getColors(rgb, cor[x]) & 0x1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //LSB替换嵌入
    //采用伪随机序列进行嵌入
    public void rand_setContent(int[] binary, String key) {
        try {
            System.out.println("嵌入率为" + binary.length * 1.0 / content.length);

            var list = generateRandomList(content.length, key);
            Random r = new Random();
            int count = 0;

            for (int i = 0; i < list.size(); i++) {
                if (count >= binary.length) {
                    if (r.nextInt() % 2 == 1)
                        content[i] = list.get(i);
                } else if (list.get(i) == 1)
                    content[i] = binary[count++];
            }

            if (count < binary.length) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == 0 && i % 2 == 1)
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

    //LSB替换嵌入
    //将图像的LSB最低位替换为更改后的LSB最低位序列
    public void setImage() {
        int height = image.getHeight(), width = image.getWidth();
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3;
                var blue = getColors(rgb, "B");
                var green = getColors(rgb, "G");
                var red = getColors(rgb, "R");

                //RGB三通道按顺序替换
                red = content[st] == 1 ? red | 0x1 : red & 0xFE;
                green = content[st + 1] == 1 ? green | 0x1 : green & 0xFE;
                blue = content[st + 2] == 1 ? blue | 0x1 : blue & 0xFE;
                rgb = (rgb >> 24) << 24 | red << 16 | green << 8 | blue;
                image.setRGB(k, i, rgb);
            }
        }
    }

    //LSB匹配嵌入
    //选择是否采用伪随机序列进行嵌入
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

            System.out.println("嵌入率为" + binary.length * 1.0 / (height * width * 3));
        }

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3;
                if (st >= bigBin.length)
                    break;

                //获取RGB三通道的值
                int blue = getColors(rgb, "B");
                int green = getColors(rgb, "G");
                int red = getColors(rgb, "R");

                //经过LSB匹配嵌入后替换图像RGB值
                rgb = (rgb >> 24) << 24 | setColor(bigBin[st], red) << 16 |
                        setColor(bigBin[st + 1], green) << 8 |
                        setColor(bigBin[st + 2], blue);
                image.setRGB(k, i, rgb);
            }
        }
    }

    //LSB匹配嵌入算法
    //如果最低位LSB值不相同，随机加减1
    //反之不作改变
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

    //LSB差分嵌入
    //选择是否采用伪随机序列进行嵌入
    public void setDiff_Content(int[] binary, String key, boolean random) {
        int height = image.getHeight(), width = image.getWidth();
        int[] bigBin;

        if (random) {
            bigBin = new int[height * width * 3];

            //获取图像的差分值
            int[] diff = getDiff(null, false);
            var list = generateRandomList(diff.length, key);
            Random r = new Random();
            int count = 0;

            //替换图像的差分值
            for (int i = 0; i < list.size(); i++) {
                if (count >= binary.length) {
                    if (r.nextInt() % 2 == 1)
                        diff[i] = list.get(i);
                } else if (list.get(i) == 1)
                    diff[i] = binary[count++];
            }

            if (count < binary.length) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == 0 && i % 2 == 1)
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

            System.out.println("嵌入率为" + binary.length * 1.0 / (height * width * 3));
        }

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), st = (i * width + k) * 3, f_blue;
                if (st >= bigBin.length)
                    break;

                //获取RGB三通道的值以及前一像素B通道的值
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

                //按差分值对RGB通道进行按顺序嵌入
                red = setDiff(f_blue, red, bigBin[st]);
                green = setDiff(red, green, bigBin[st + 1]);
                blue = setDiff(green, blue, bigBin[st + 2]);
                rgb = rgb << 24 | red << 16 | green << 8 | blue;
                image.setRGB(k, i, rgb);
            }
        }
    }

    //LSB差分嵌入算法
    //如果差分值不相同，则随机对后一个像素值进行随机加减1
    //反之不做改变
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

    //获取图像的差分值
    //选择是否采用伪随机序列，只用于提取
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
                if (list.get(i) == 0 && i % 2 == 1)
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
