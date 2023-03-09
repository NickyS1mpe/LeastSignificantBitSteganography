package LSB.web.Function;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName: IO
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 17:47 2023/3/8
 **/
public class IO {

    public BufferedImage readPic(String road) throws IOException {
        return ImageIO.read(new File(road));
    }

    public void writePic(BufferedImage image, String road) throws IOException {
        String format = road.substring(road.lastIndexOf(".") + 1);
        ImageIO.write(image, format, new File(road));
    }

}
