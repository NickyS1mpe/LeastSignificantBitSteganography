package LSB.web.Function;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName: EdgeAdaptive
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 17:03 2023/3/16
 **/
public class EdgeAdaptive extends IO {
    BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(String road) {
        this.image = readPic(road);
    }

    /**
     * @Author: Nick Lee
     * @Description: divide into groups
     * @Date: 2023/3/17 20:42
     * @Return:
     **/
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

    /**
     * @Author: Nick Lee
     * @Description: analyze groups
     * @Date: 2023/3/17 20:42
     * @Return:
     **/
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

    private int[] analyze_Sobel(int[][][] group, int threshold1, int threshold2) {
        int[] res = new int[group.length];
        int[][] gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] gy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
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
            } else if (magnitude > threshold2) {
                res[i] += 1;
            }
        }
        return res;
    }

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

    private void insertMes(int n, int[][] pos, int[] binary, List<Integer> ran) {
        int height = image.getHeight(), width = image.getWidth(), count = 0, count1 = 0, count2 = 0, count3 = 0;
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
                    for (int z = 1; z < 3; z++) {
                        if (pos[z][count] <= 1) {
                            if (ran.get(count2) == 1) {
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
                            count2++;
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
        System.out.println();
    }

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

    private ArrayList<Integer> generateRandomList(int n, double proportion) {
        ArrayList<Integer> list = new ArrayList<>();
        var random = new Random();
        int zero = (int) (n * (1 - proportion));
        for (int i = 0; i < n; i++) {
            if (i < zero) {
                list.add(0);
            } else {
                list.add(1);
            }
        }
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
        return list;
    }

    private int countType(int[] mag) {
        int res = 0;
        for (int i : mag) {
            if (i < 2)
                res++;
        }
        return res;
    }

    public void find(int n, int threshold1, int threshold2, String road1, String road2) {
        String[] rgb = {"R", "G", "B"};
        String[] r = road1.split("\\\\");
        String path = road2 + r[r.length - 1].split("\\.")[0];
        int[][] res = new int[3][];
        setImage(road1);
        for (int i = 0; i < rgb.length; i++) {
            int[][][] org = getGroup(n, rgb[i]);
            res[i] = analyze_Sobel(org, threshold1, threshold2);
            createFolder(path);
            highlightImg(n, res[i], rgb[i], path + "\\" + rgb[i] + "_" + r[r.length - 1]);
        }
    }

    public void insert(int n, int threshold1, int threshold2, int[] binary, String road1, String road2) {
        String[] rgb = {"R", "G", "B"};
        int[][] res = new int[3][];
        int require = binary.length / (n * n) + 1;
        setImage(road1);
        for (int i = 1; i < rgb.length; i++) {
            int[][][] org = getGroup(n, rgb[i]);
            res[i] = analyze_Sobel(org, threshold1, threshold2);
        }
        int G = countType(res[1]) - 1, B = countType(res[2]) - 1;
        double insertRate = require * 1.0 / (G + B);
        List<Integer> ranList = generateRandomList(G + B, insertRate);
        System.out.println("嵌入率为" + insertRate);
        insertMes(n, res, binary, ranList);
        writePic(image, road2);
    }

    public int[] get(int n, String road1) {
        setImage(road1);
//        for (int i = 0; i < 1; i++) {
//            int[][][] org = getGroup(n, rgb[i]);
//            res[i] = analyze_Sobel(org, threshold1, threshold2);
//        }
        return getContent(n);
    }
}
