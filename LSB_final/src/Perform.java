import java.awt.*;
import java.awt.image.BufferedImage;

//图像性能评估程序
public class Perform extends IO {
    BufferedImage org, pos;

    //Mean Square Error，MSE分析
    public double MSE(String read1, String read2) {
        try {
            org = readPic(read1);
            pos = readPic(read2);
            int height = org.getHeight(), width = org.getWidth();
            double MSE, MSE_R = 0, MSE_G = 0, MSE_B = 0;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color o = new Color(org.getRGB(j, i)), p = new Color(pos.getRGB(j, i));
                    int o_r = o.getRed(), o_g = o.getGreen(), o_b = o.getBlue();
                    int p_r = p.getRed(), p_g = p.getGreen(), p_b = p.getBlue();
                    MSE_R += Math.pow(o_r - p_r, 2);
                    MSE_G += Math.pow(o_g - p_g, 2);
                    MSE_B += Math.pow(o_b - p_b, 2);
                }
            }

            MSE = (MSE_R + MSE_G + MSE_B) / (height * width);
            return MSE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Peak Signal to Noise Ration，PSNR分析
    public double PSNR(String read1, String read2) {
        try {
            if (read1.equals(read2))
                return 0;

            double mse = MSE(read1, read2);
            double max = Math.pow(255, 2);
            double psnr = Math.log10(max / mse);
            return 10 * psnr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //图像比对
    //对应位置像素值相同则置为白色，反之置为黑色
    public void compare(String read1, String read2, String write) {
        try {
            org = readPic(read1);
            pos = readPic(read2);
            BufferedImage com = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

            for (int j = 0; j < org.getHeight(); j++) {
                for (int k = 0; k < org.getWidth(); k++) {
                    if (org.getRGB(k, j) != pos.getRGB(k, j)) {
                        com.setRGB(k, j, new Color(255, 255, 255).getRGB());
                    } else {
                        com.setRGB(k, j, new Color(0, 0, 0).getRGB());
                    }
                }
            }

            writePic(com, write);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
