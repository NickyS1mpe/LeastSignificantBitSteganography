package LSB.web;

import LSB.web.Function.BinaryPic;
import LSB.web.Function.LSB_Color;
import LSB.web.Function.Perform;
import LSB.web.Function.mainService;
import LSB.web.Service.webService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @ClassName: LSB.web.usage_test
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 11:36 2023/2/22
 **/
@SpringBootTest
public class usage_test {

    @Test
    void testLSB() {
        try {
            String key = "asdfghjklzxcvbnm";
            String road1 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\Node.js_logo.png",
                    road2 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\nS4.png",
                    road3 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\test2.txt",
                    road4 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\nS5.png",
                    road5 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\testBIN.png";
//            new mainService().LSB_Control("r", key, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\testMES.TXT");
//            new webService().encryptPic(key, road1, road2, road3);
//            new webService().decryptPic(key, road2, road3);
//            BinaryPic bin = new BinaryPic();
//            bin.setImageBin(road1, road5, road4);
//            bin.getBinFromImage(road4, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\nS6.png");
            System.out.println(new webService().findBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
