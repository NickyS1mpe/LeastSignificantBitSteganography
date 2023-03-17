package LSB.web.Function;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: RS_Analyze
 * @Description: RS Analyze
 * @Author: Nick Lee
 * @Date: Create in 17:19 2023/3/14
 **/
public class RS_Analyze extends IO {
    BufferedImage img;

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(String road) {
        this.img = readPic(road);
    }

    /**
     * @Author: Nick Lee
     * @Description: F1& F-1 reverse
     * @Date: 2023/3/14 20:26
     * @Return:
     **/
    private int reverse(int cur, int flag) {
        if (flag == 1) {
            if ((cur % 2) == 1)
                cur--;
            else
                cur++;
        } else if (flag == -1) {
            if ((cur % 2) == 1)
                cur++;
            else
                cur--;
        }
        return cur;
    }

    /**
     * @Author: Nick Lee
     * @Description: group reverse
     * @Date: 2023/3/15 17:22
     * @Return:
     **/
    private int[][] groupRev(int[][] group, int[] m, int flag) {
        int[][] res = new int[group.length][group[0].length];
        for (int i = 0; i < group.length; i++) {
            for (int k = 0; k < group[i].length; k++) {
                if (m[(i * group[i].length + k) % m.length] == 1)
                    res[i][k] = reverse(group[i][k], flag);
                else
                    res[i][k] = reverse(group[i][k], 0);
            }
        }
        return res;
    }

    /**
     * @Author: Nick Lee
     * @Description: pixel relevance sort by line
     * @Date: 2023/3/15 17:22
     * @Return:
     **/
    private int relevance_byLine(int[][] pixel) {
        int res = 0;
        List<Integer> list = new ArrayList<>();
        for (int[] ints : pixel) {
            for (int i : ints) {
                list.add(i);
            }
        }
        for (int i = 0; i < list.size() - 1; i++) {
            res += Math.abs(list.get(i + 1) - list.get(i));
        }
        return res;
    }

    /**
     * @Author: Nick Lee
     * @Description: pixel relevance sort  by zigzag
     * @Date: 2023/3/15 21:55
     * @Return:
     **/
    private int relevance_byZ(int[][] pixel) {
        int res = 0, m = 0, n = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 2 * pixel.length - 1; i++) {
            if (i % 2 == 0) {
                for (int x = m, y = n; x >= 0 && y >= 0 && x < pixel.length && y < pixel.length; x--, y++) {
                    list.add(pixel[x][y]);
                }
                if (i < pixel.length - 1) {
                    m = m + 1;
                    var temp = m;
                    m = n;
                    n = temp;
                } else {
                    n = n + 1;
                    var temp = m;
                    m = n;
                    n = temp;
                }
            } else {
                for (int x = m, y = n; x >= 0 && y >= 0 && x < pixel.length && y < pixel.length; y--, x++) {
                    list.add(pixel[x][y]);
                }
                if (i < pixel.length - 1) {
                    n = n + 1;
                    var temp = m;
                    m = n;
                    n = temp;
                } else {
                    m = m + 1;
                    var temp = m;
                    m = n;
                    n = temp;
                }
            }
        }
        for (int i = 0; i < list.size() - 1; i++) {
            res += Math.abs(list.get(i + 1) - list.get(i));
        }
        return res;
    }

    /**
     * @Author: Nick Lee
     * @Description: divide into groups
     * @Date: 2023/3/15 17:22
     * @Return:
     **/
    private int[][][] getGroup(int n, String col) {
        int height = img.getHeight(), width = img.getWidth(), count = 0;
        int[][][] group = new int[(height - n + 1) * (width - n + 1)][n][n];
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                if (k + (n - 1) < width && i + (n - 1) < height) {
                    for (int x = 0; x < n; x++) {
                        for (int y = 0; y < n; y++) {
                            group[count][x][y] = getColors(img.getRGB(k + y, i + x), col);
                        }
                    }
                    count++;
                }
            }
        }
        /*int h = height % 8 == 0 ? height / 8 : height / 8 + 1, w = width % 8 == 0 ? width / 8 : width / 8 + 1;
        int[][][] group = new int[h * w][n][n];
        for (int i = 0; i < h; i++) {
            for (int k = 0; k < w; k++) {
                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < n; y++) {
                        group[count][x][y] = 8 * k + y > width || 8 * i + x > height ? 0 : getColors(img.getRGB(8 * k + y, 8 * i + x), col);
                    }
                }
                count++;
            }
        }*/
        return group;
    }

    /**
     * @Author: Nick Lee
     * @Description: count Rm,R-m,Sm,S-m
     * @Date: 2023/3/15 17:22
     * @Return:
     **/
    public double[] count(int n, String color, boolean reverse) {
        int[][][] org = getGroup(n, color);
        if (reverse) {
            for (int i = 0; i < org.length; i++) {
                for (int j = 0; j < org[i].length; j++) {
                    for (int k = 0; k < org[i][j].length; k++) {
                        org[i][j][k] ^= 1;
                    }
                }
            }
        }
        int[][][] F1 = org.clone(), F_1 = org.clone();
        int[] m = new int[]{0, 1, 1, 0};
        int Rm = 0, R_m = 0, Sm = 0, S_m = 0;
        for (int i = 0; i < org.length; i++) {
            F1[i] = groupRev(F1[i], m, 1);
            F_1[i] = groupRev(F_1[i], m, -1);
            int rel_org = relevance_byZ(org[i]), rel = relevance_byZ(F1[i]), rel_1 = relevance_byZ(F_1[i]);
            if (rel > rel_org)
                Rm++;
            if (rel_1 > rel_org)
                R_m++;
            if (rel < rel_org)
                Sm++;
            if (rel_1 < rel_org)
                S_m++;
        }
/*        System.out.println("Rm=" + Rm);
        System.out.println("R_m=" + R_m);
        System.out.println("Sm=" + Sm);
        System.out.println("S_m=" + S_m);
        System.out.println("Rm-R_m=" + (Rm - R_m));
        System.out.println("Sm-S_m=" + (Sm - S_m));
        System.out.println("Rm-Sm=" + (Rm - Sm));
        System.out.println("R_m-S_m=" + (R_m - S_m));*/
        double RmD = Rm * 1.0 / org.length;
        double R_mD = R_m * 1.0 / org.length;
        double SmD = Sm * 1.0 / org.length;
        double S_mD = S_m * 1.0 / org.length;
/*        System.out.println("RmD=" + RmD);
        System.out.println("R_mD=" + R_mD);
        System.out.println("SmD=" + SmD);
        System.out.println("S_mD=" + S_mD);
        System.out.println("RmD-R_mD=" + (RmD - R_mD));
        System.out.println("SmD-S_mD=" + (SmD - S_mD));
        System.out.println("RmD-SmD=" + (RmD - SmD));
        System.out.println("R_mD-S_mD=" + (R_mD - S_mD));*/
        return new double[]{RmD, R_mD, SmD, S_mD};
    }

    public double countRate(double[][] rs) {
        double d0 = rs[0][0] - rs[0][2], d1 = rs[1][0] - rs[1][2], d2 = rs[0][1] - rs[0][3], d3 = rs[1][1] - rs[1][3];
        double a = 2 * (d0 + d1), b = d2 - d3 - d1 + (-3) * d0, c = d0 - d2, delta = Math.sqrt(b * b - 4 * a * c);
        double x1 = ((-1) * b + delta) / (2.0 * a), x2 = ((-1) * b - delta) / (2.0 * a);
        return Math.min(x1 / (x1 - 0.5), x2 / (x2 - 0.5));
    }

    public void judge(int n, double threshold, double ml0, String road) {
        setImg(road);
        String[] RGB = {"R", "G", "B"};
        double[][] res = new double[2][4];
        for (String s : RGB) {
            double[] tmp = count(n, s, false);
            for (int i = 0; i < tmp.length; i++) {
                res[0][i] += tmp[i];
            }
        }
        for (String s : RGB) {
            double[] tmp = count(n, s, true);
            for (int i = 0; i < tmp.length; i++) {
                res[1][i] += tmp[i];
            }
        }
        double rate = countRate(res);
        double rs = res[0][0] - res[0][2], rs_ = res[0][1] - res[0][3],
                rate1 = Math.abs(res[0][0] - res[0][1]) / res[0][0],
                rate2 = Math.abs(res[0][2] - res[0][3]) / res[0][2];
        if (res[0][0] > res[0][2] && res[0][1] > res[0][3] && rate1 < threshold && rate2 < threshold) {
            System.out.println("可能未隐写");
            System.out.println("偏差为：" + rate);
        } else if (rs_ > rs) {
            System.out.println("可能存在LSB隐写");
            rate = (rate - ml0) / (1 - ml0);
            System.out.println("隐写率为：" + rate);
        }
    }
}
