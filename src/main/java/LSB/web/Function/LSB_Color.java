package LSB.web.Function;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @ClassName: LSB_Color
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 16:47 2023/2/24
 **/
public class LSB_Color {
    BufferedImage image;
    int[] content;

    public LSB_Color(BufferedImage image) {
        this.image = image;
        this.content = new int[image.getHeight() * image.getWidth() * 8];
    }

    /**
     * @Author: Nick Lee
     * @Description: get RGB final bit from image
     * @Date: 2023/3/1 9:39
     * @Return:
     **/
    public int[] getColor() {
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

    public void setContent(int[] binary) {
        try {
            System.arraycopy(binary, 0, content, 0, binary.length);
            for (int i = binary.length; i < content.length; i++) {
                content[i] = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getContent() {
        return content;
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
}
