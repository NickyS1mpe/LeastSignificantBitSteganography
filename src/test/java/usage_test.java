import Function.Decrypt;
import Function.Encrypt;
import Function.LSB_Color;
import Service.mainService;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @ClassName: usage_test
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 11:36 2023/2/22
 **/
public class usage_test {

    @Test
    public void main() {
//        var test = new Encrypt("C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\logo.png", "test");
        try {
//            BufferedImage image = ImageIO.read(new File("C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\logo.png"));
//            LSB_Color test = new LSB_Color(image);
//            String s = "test";
//            Encrypt en = new Encrypt(s);
//            en.Encode();
//            test.setContent(en.getBinary());
//            test.setImage();
//            try {
//                String file = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\encrypt.png";
//                String format = file.substring(file.lastIndexOf(".") + 1);
//                ImageIO.write(test.getImage(), format, new File(file));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            BufferedImage image = ImageIO.read(new File("C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\encrypt.png"));
//            new mainService().LSB_Control("w", "testLSB", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\logo.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\LSB.png");
            new mainService().LSB_Control("r", "", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\LSB.png", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
