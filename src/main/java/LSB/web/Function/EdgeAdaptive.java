package LSB.web.Function;

import java.awt.image.BufferedImage;

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
        int h = height % 8 == 0 ? height / 8 : height / 8 + 1, w = width % 8 == 0 ? width / 8 : width / 8 + 1;
        int[][][] group = new int[h * w][n][n];
        for (int i = 0; i < h; i++) {
            for (int k = 0; k < w; k++) {
                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < n; y++) {
                        group[count][x][y] = 8 * k + y > width || 8 * i + x > height ? 0 : getColors(image.getRGB(8 * k + y, 8 * i + x), col);
                    }
                }
                count++;
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
    private int[] analyze(int[][][] group) {
        int[] res = new int[group.length];

        return res;
    }
}
