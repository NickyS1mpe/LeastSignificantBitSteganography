package LSB.web.Function;

import org.hibernate.jdbc.Work;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @ClassName: Perform
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 17:46 2023/3/8
 **/
public class Perform extends IO {
    BufferedImage org, pos;

    /**
     * @Author: Nick Lee
     * @Description: Mean Square Error
     * @Date: 2023/3/8 18:02
     * @Return:
     **/
    public double MSE(String read1, String read2) throws IOException {
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
    }

    /**
     * @Author: Nick Lee
     * @Description: Peak Signal to Noise Ration
     * @Date: 2023/3/8 18:03
     * @Return:
     **/
    public double PSNR(String read1, String read2) throws IOException {
        double mse = MSE(read1, read2);
        double max = Math.pow(255, 2);
        double psnr = Math.log10(max / mse);
        return 10 * psnr;
    }
}
