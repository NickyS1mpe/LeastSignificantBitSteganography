import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//基于图像边缘检测的LSB隐写
public class EdgeAdaptive extends IO {
    BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(String road) {
        this.image = readPic(road);
    }

    //分组读取像素值，用于后续边缘检测分析
    //组之间不重叠
    //默认分组大小为3x3像素，按行读取
    private int[][][] getGroup(int n, String col) {
        int height = image.getHeight(), width = image.getWidth(), count = 0;
        int h = height % n == 0 ? height / n : height / n + 1, w = width % n == 0 ? width / n : width / n + 1;
        int[][][] group = new int[(height / n) * (width / n) + 1][n][n];
        for (int i = 0; i <= h; i++) {
            for (int k = 0; k <= w; k++) {
                if (n * k + n <= width && n * i + n <= height) {
                    for (int x = 0; x < n; x++) {
                        for (int y = 0; y < n; y++) {
                            group[count][x][y] = getColors(image.getRGB(n * k + y, n * i + x), col);
                        }
                    }
                    count++;
                }
            }
        }
        return group;
    }

    //分组读取像素值，用于后续边缘检测分析
    //按卷积进行像素值读取，组之间存在重叠
    //超出边缘部分记录像素值为0
    //默认分组大小为3x3像素，按行读取
    private int[][][] getGroup_byPixel(int n, String col) {
        int height = image.getHeight(), width = image.getWidth();
        int[][][] group = new int[height * width][n][n];
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        int rgb;
                        if (k + y < 0 || i + x < 0 || k + y >= width || i + x >= height) {
                            rgb = 0;
                        } else
                            rgb = getColors(image.getRGB(k + y, i + x), col);
                        group[i * width + k][1 + x][1 + y] = rgb;
                    }
                }
            }
        }
        return group;
    }

    //按每组的中心像素进行平滑度分析
    //分析结果分为三类：平滑，一般锐利和锐利，由上阈值和下阈值决定
    //分析结果按组进行记录
    private int[] analyze(int[][][] group, int threshold1, int threshold2) {
        int[] res = new int[group.length];
        int count0 = 0, count1 = 0, count2 = 0;
        for (int i = 0; i < group.length; i++) {
            int mid = group[i][group[i].length / 2][group[i].length / 2], tmp = 0;
            for (int j = 0; j < group[i].length; j++) {
                for (int k = 0; k < group[i][j].length; k++) {
                    tmp += Math.abs(group[i][j][k] - mid);
                }
            }
            tmp /= group[i].length * group[i].length;
            if (tmp > threshold1) {
                count2++;
                res[i] += 2;
            } else if (tmp > threshold2) {
                count1++;
                res[i] += 1;
            } else
                count0++;
        }
        System.out.println(count0 + "||" + count1 + "||" + count2);
        return res;
    }

    //按每组的中心像素进行Sobel边缘检测分析
    //分析结果分为三类：平滑，一般锐利和锐利，由上阈值和下阈值决定
    //分析结果按单个像素值进行记录
    private int[] analyze_Sobel(int[][][] group, int threshold1, int threshold2) {
        int[] res = new int[group.length];

        //Sobel算子
        int[][] gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] gy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        int count1 = 0, count2 = 0;
        for (int i = 0; i < group.length; i++) {
            int x = 0, y = 0;
            for (int j = 0; j < group[i].length; j++) {
                for (int k = 0; k < group[i][j].length; k++) {
                    x += group[i][j][k] * gx[j][k];
                    y += group[i][j][k] * gy[j][k];
                }
            }
            double magnitude = Math.sqrt(x * x + y * y);
            if (magnitude > threshold1) {
                res[i] += 2;
                count2++;
            } else if (magnitude > threshold2) {
                res[i] += 1;
                count1++;
            }
        }

        System.out.println("Not so sharp: " + count1 + "  +  " + "sharp: " + count2);
        return res;
    }

    //按照分析结果对图片R、G、B通道分别按组进行标亮
    //用于平滑度分析后处理
    private void highlightImg(int n, int[] res, String col, String road) {
        int height = image.getHeight(), width = image.getWidth(), count = 0;
        int h = height % n == 0 ? height / n : height / n + 1, w = width % n == 0 ? width / n : width / n + 1;

        for (int i = 0; i <= h; i++) {
            for (int k = 0; k <= w; k++) {
                if (res[count] >= 1) {
                    for (int x = 0; x < n; x++) {
                        for (int y = 0; y < n; y++) {
                            if (n * k + y < width && n * i + x < height) {
                                var rgb = image.getRGB(n * k + y, n * i + x);
                                int red = getColors(rgb, "R"), green = getColors(rgb, "G"), blue = getColors(rgb, "B");
                                var alt = getColors(rgb, col);
                                alt &= 0x001;
                                if (res[count] == 1)
                                    rgb = rgb << 24 | alt << 16 | green << 8 | blue;
                                else
                                    rgb = rgb << 24 | alt << 16 | alt << 8 | blue;
                                image.setRGB(n * k + y, n * i + x, rgb);
                            }
                        }
                    }
                }
                count++;
            }
        }
        writePic(image, road);
    }

    //按照分析结果对图片R、G、B通道分别绘制二值图像
    //用于Sobel边缘分析后处理
    //锐利点标记为白色，一般锐利点标记为灰色，非边缘点标记为黑色
    private void highlightImg_byPixel(int[] res, String road) {
        int height = image.getHeight(), width = image.getWidth(), count = 0;
        BufferedImage ai = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                if (res[count] >= 1) {
                    int gray;
                    if (res[count] == 1)
                        gray = new Color(180, 180, 180).getRGB();
                    else
                        gray = new Color(255, 255, 255).getRGB();
                    ai.setRGB(k, i, colorToRGB(0, gray, gray, gray));
                }
                count++;
            }
        }
        writePic(ai, road);
    }

    //将Alpha、R、G、B值转换为RGB值
    private int colorToRGB(int alpha, int red, int green, int blue) {
        return alpha << 24 |
                red << 16 |
                green << 8 |
                blue;
    }

    //信息嵌入，嵌入G、B通道，R通道记录嵌入信息，根据随机列表list选择是否嵌入
    //用于按组进行的平滑度分析算法
    private void insertMes(int n, int[][] pos, int[] binary, List<Integer> ran) {
        int height = image.getHeight(), width = image.getWidth(), count = 0, count1 = 0, count2 = 0;
        int h = height % n == 0 ? height / n : height / n + 1, w = width % n == 0 ? width / n : width / n + 1;
        var end = false;
        for (int i = 0; i <= h; i++) {
            if (end)
                break;
            for (int k = 0; k <= w; k++) {
                if (end)
                    break;
                if (n * k + n <= width && n * i + n <= height) {
                    boolean iG = false, iB = false;
                    if (!(pos[1][count] == 0 && pos[2][count] == 0) || !(pos[1][count] == 2 && pos[2][count] == 2)) {
                        for (int z = 1; z < 3; z++) {
                            if (ran.get(count2++) == 1) {
                                String col = "";
                                switch (z) {
                                    case 1:
                                        col = "G";
                                        iG = true;
                                        break;
                                    case 2:
                                        col = "B";
                                        iB = true;
                                        break;
                                }
                                for (int x = 0; x < n; x++) {
                                    if (end)
                                        break;
                                    for (int y = 0; y < n; y++) {
                                        if (count1 >= binary.length) {
                                            end = true;
                                            break;
                                        } else {
                                            int rgb = image.getRGB(n * k + y, n * i + x);
                                            int a = binary[count1], cur = getColors(rgb, col);
                                            cur = setColor(a, cur);
                                            image.setRGB(n * k + y, n * i + x, setRGB(rgb, cur, col));
                                            count1++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    int cur, cur1, rgb = image.getRGB(n * k, n * i), rgb1 = image.getRGB(n * k + 1, n * i + 1);
                    if (iG && iB) {
                        cur = setColor(1, getColors(rgb, "R"));
                        cur1 = setColor(1, getColors(rgb1, "R"));
                    } else if (iG) {
                        cur = setColor(1, getColors(rgb, "R"));
                        cur1 = setColor(0, getColors(rgb1, "R"));
                    } else if (iB) {
                        cur = setColor(0, getColors(rgb, "R"));
                        cur1 = setColor(1, getColors(rgb1, "R"));
                    } else {
                        cur = setColor(0, getColors(rgb, "R"));
                        cur1 = setColor(0, getColors(rgb1, "R"));
                    }
                    image.setRGB(n * k, n * i, setRGB(rgb, cur, "R"));
                    image.setRGB(n * k + 1, n * i + 1, setRGB(rgb1, cur1, "R"));
                    count++;
                }
            }
        }
    }

    //信息嵌入，嵌入G、B通道，R通道记录嵌入信息，根据随机列表list选择是否嵌入
    //当G和B通道的值都为0时不嵌入，视为过于平滑
    //当G和B通道的值都为2时不嵌入，视为过于锐利
    //当G或B通道为0时，嵌入1位；为1时嵌入2位；为2时嵌入3位
    //R通道取3位记录G和B通道的嵌入情况，分别有8种情况
    //用于按像素进行Sobel边缘分析后隐写
    private void insertMes_byPixel(int[][] pos, int[] binary, List<Integer> ran) {
        int height = image.getHeight(), width = image.getWidth(), count1 = 0, count2 = 0;
        var end = false;
        for (int i = 0; i < height; i++) {
            if (end)
                break;
            for (int k = 0; k < width; k++) {
                if (end)
                    break;

                //G、B通道LSB多位嵌入
                boolean sG = false, sB = false, dG = false, dB = false, tG = false, tB = false;
                if (!(pos[1][i * width + k] == 0 && pos[2][i * width + k] == 0) && !(pos[1][i * width + k] == 2 && pos[2][i * width + k] == 2)) {
                    if (ran.get(count2++) == 1) {
                        for (int z = 1; z < 3; z++) {
                            String col = "";
                            switch (z) {
                                case 1:
                                    col = "G";
                                    switch (pos[z][i * width + k]) {
                                        case 0:
                                            sG = true;
                                            break;
                                        case 1:
                                            dG = true;
                                            break;
                                        case 2:
                                            tG = true;
                                            break;
                                    }
                                    break;
                                case 2:
                                    col = "B";
                                    switch (pos[z][i * width + k]) {
                                        case 0:
                                            sB = true;
                                            break;
                                        case 1:
                                            dB = true;
                                            break;
                                        case 2:
                                            tB = true;
                                            break;
                                    }
                                    break;
                            }

                            if (count1 >= binary.length) {
                                end = true;
                                break;
                            } else {
                                int rgb = image.getRGB(k, i), cur = getColors(rgb, col);

                                //嵌入一位
                                if ((col.equals("B") && sB) || (col.equals("G") && sG)) {
                                    cur = setColor(binary[count1++], cur);
                                }

                                //嵌入两位
                                else if ((col.equals("B") && dB) || (col.equals("G") && dG)) {
                                    cur = setColor(binary[count1++], cur);
                                    if (count1 < binary.length && binary[count1++] == 1) {
                                        cur = (setColor(1, cur >> 1) << 1) | (cur & 0x1);
                                    } else
                                        cur = (setColor(0, cur >> 1) << 1) | (cur & 0x1);
                                }

                                //嵌入三位
                                else if ((col.equals("B") && tB) || (col.equals("G") && tG)) {
                                    cur = setColor(binary[count1++], cur);
                                    if (count1 < binary.length && binary[count1++] == 1) {
                                        cur = (setColor(1, cur >> 1) << 1) | (cur & 0x1);
                                    } else
                                        cur = (setColor(0, cur >> 1) << 1) | (cur & 0x1);
                                    if (count1 < binary.length && binary[count1++] == 1) {
                                        cur = (setColor(1, cur >> 2) << 2) | (cur & 0x3);
                                    } else
                                        cur = (setColor(0, cur >> 2) << 2) | (cur & 0x3);
                                }

                                //替换嵌入后的RGB值
                                image.setRGB(k, i, setRGB(rgb, cur, col));
                            }
                        }
                    }
                }

                //R通道嵌入G、B通道的嵌入信息
                //如果G、B通道都没有嵌入则记录为000
                int rgb = image.getRGB(k, i), cur = getColors(rgb, "R"), fin = 0;

                //010
                if (sG && dB) {
                    fin |= 0x2;
                }

                //011
                if (dG && sB) {
                    fin |= 0x3;
                }

                //111
                if (dG && dB) {
                    fin |= 0x7;
                }

                //110
                if (dG && tB) {
                    fin |= 0x6;
                }

                //010
                if (tG && dB) {
                    fin |= 0x4;
                }

                //101
                if (tG && sB) {
                    fin |= 0x5;
                }

                //001
                if (sG && tB) {
                    fin |= 0x1;
                }
                cur = setColor(fin & 0x1, cur);
                if (((fin >> 1) & 0x1) == 1) {
                    cur = (setColor(1, cur >> 1) << 1) | (cur & 0x1);
                } else
                    cur = (setColor(0, cur >> 1) << 1) | (cur & 0x1);
                if (((fin >> 2) & 0x1) == 1) {
                    cur = (setColor(1, cur >> 2) << 2) | (cur & 0x3);
                } else
                    cur = (setColor(0, cur >> 2) << 2) | (cur & 0x3);

                image.setRGB(k, i, setRGB(rgb, cur, "R"));
            }
        }
    }

    //信息提取
    //用于按组进行的平滑度分析算法
    private int[] getContent(int n) {
        int height = image.getHeight(), width = image.getWidth(), count1 = 0, count3 = 0, count4 = 0;
        int h = height % n == 0 ? height / n : height / n + 1, w = width % n == 0 ? width / n : width / n + 1;
        int[] binary = new int[height * width * 2];
        for (int i = 0; i <= h; i++) {
            for (int k = 0; k <= w; k++) {
                if (n * k + n <= width && n * i + n <= height) {
                    int rgb = image.getRGB(n * k, n * i), rgb1 = image.getRGB(n * k + 1, n * i + 1);
                    if ((getColors(rgb, "R") & 0x1) == 1) {
                        count3++;
                        String col = "G";
                        for (int x = 0; x < n; x++) {
                            for (int y = 0; y < n; y++) {
                                var tmp = image.getRGB(n * k + y, n * i + x);
                                var cur = getColors(tmp, col);
                                binary[count1] = cur & 0x1;
                                count1++;
                            }
                        }
                    }
                    if ((getColors(rgb1, "R") & 0x1) == 1) {
                        count4++;
                        String col = "B";
                        for (int x = 0; x < n; x++) {
                            for (int y = 0; y < n; y++) {
                                var tmp = image.getRGB(n * k + y, n * i + x);
                                var cur = getColors(tmp, col);
                                binary[count1] = cur & 0x1;
                                count1++;
                            }
                        }
                    }
                }
            }
        }
        return binary;
    }

    //信息提取
    //用于按像素进行Sobel边缘分析后隐写的提取
    //根据R通道中记录的嵌入情况进行提取
    private int[] getContent_byPixel() {
        int height = image.getHeight(), width = image.getWidth(), count1 = 0;
        int[] binary = new int[height * width * 4 + 1];
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                int rgb = image.getRGB(k, i), red = getColors(rgb, "R"), dat = red & 0x7;

                if (dat == 2) {
                    String col = "G";
                    var cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    col = "B";
                    cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                }

                if (dat == 3) {
                    String col = "G";
                    var cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                    col = "B";
                    cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                }

                if (dat == 7) {
                    String col = "G";
                    var cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                    col = "B";
                    cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                }

                if (dat == 6) {
                    String col = "G";
                    var cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                    col = "B";
                    cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                    binary[count1++] = (cur >> 2) & 0x1;
                }

                if (dat == 4) {
                    String col = "G";
                    var cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                    binary[count1++] = (cur >> 2) & 0x1;
                    col = "B";
                    cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                }

                if (dat == 5) {
                    String col = "G";
                    var cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                    binary[count1++] = (cur >> 2) & 0x1;
                    col = "B";
                    cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                }

                if (dat == 1) {
                    String col = "G";
                    var cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    col = "B";
                    cur = getColors(rgb, col);
                    binary[count1++] = cur & 0x1;
                    binary[count1++] = (cur >> 1) & 0x1;
                    binary[count1++] = (cur >> 2) & 0x1;
                }

            }
        }
        return binary;
    }

    //采用LSB匹配进行嵌入
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

    //按比例生成随机序列
    //随机序列只包含0和1，比例为1的数量占序列总长的比值
    private ArrayList<Integer> generateRandomList(int n, double proportion) {
        ArrayList<Integer> list = new ArrayList<>();
        var random = new Random();

        //按比例生成相对应的0和1值
        int zero = (int) (n * (1 - proportion));
        for (int i = 0; i < n; i++) {
            if (i < zero) {
                list.add(0);
            } else {
                list.add(1);
            }
        }

        //打乱生成的序列值，使其变为随机序列
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }

        return list;
    }

    //检测结果统计
    //用于计算有效载荷容量
    //按嵌入策略进行统计
    private int countType(int[][] mag) {
        int res = 0;

        //计算G通道
        for (int i = 0; i < mag[1].length; i++) {
            if (!(mag[1][i] == 0 && mag[2][i] == 0) && !(mag[1][i] == 2 && mag[2][i] == 2))
                if (mag[1][i] < 1)
                    res++;
                else if (mag[1][i] == 1)
                    res += 2;
                else
                    res += 3;
        }

        //计算B通道
        for (int i = 0; i < mag[2].length; i++) {
            if (!(mag[1][i] == 0 && mag[2][i] == 0) && !(mag[1][i] == 2 && mag[2][i] == 2))
                if (mag[2][i] < 1)
                    res++;
                else if (mag[2][i] == 1)
                    res += 2;
                else
                    res += 3;
        }
        return res;
    }

    //进行边缘检测分析的函数
    //默认生成按原始文件命名的文件夹，用于存储分析结果
    public void find(int n, int threshold1, int threshold2, String road1, String road2) {
        String[] rgb = {"R", "G", "B"};
        String[] r = road1.split("\\\\");
        String path = road2 + r[r.length - 1].split("\\.")[0];
        int[][] res = new int[3][];

        setImage(road1);
        createFolder(path);

        //按照RGB三通道分别进行检测
        for (int i = 0; i < rgb.length; i++) {
            int[][][] org = getGroup_byPixel(n, rgb[i]);
            res[i] = analyze_Sobel(org, threshold1, threshold2);
            highlightImg_byPixel(res[i], path + "\\" + rgb[i] + "_" + r[r.length - 1]);
        }
    }

    //进行信息嵌入的函数
    //偏移值用于控制生成的随机序列的长度
    //如果偏移值超过有效载荷容量则采用最大容量值
    public void insert(int n, int threshold1, int threshold2, int deviation, int[] binary, String road1, String road2) {
        String[] rgb = {"R", "G", "B"};
        int[][] res = new int[3][];
        int require = binary.length + 1;

        setImage(road1);
        for (int i = 1; i < rgb.length; i++) {
            int[][][] org = getGroup_byPixel(n, rgb[i]);
            System.out.println(rgb[i] + " channel: ");
            res[i] = analyze_Sobel(org, threshold1, threshold2);
        }
        int totalPixel = countType(res);

        //计算嵌入率
        double insertRate = require * 1.0 / totalPixel;
        int total = Math.min(require + deviation, totalPixel);
        List<Integer> ranList = generateRandomList(total, require * 1.0 / total);

        System.out.println("嵌入率为" + insertRate);

        insertMes_byPixel(res, binary, ranList);
        writePic(image, road2);
    }

    //进行信息获取的函数
    public int[] get(String road1) {
        setImage(road1);
//        for (int i = 0; i < 1; i++) {
//            int[][][] org = getGroup(n, rgb[i]);
//            res[i] = analyze_Sobel(org, threshold1, threshold2);
//        }
        int[] s = getContent_byPixel();
        return s;
    }
}
