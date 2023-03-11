package LSB.web;

import LSB.web.Service.webService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
            String road1 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\pic\\Node.js_logo.png",
                    road2 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\BR1lsb.png",
                    road3 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\txt\\mes.TXT",
                    road4 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resTXT\\M3lsb.TXT",
                    road5 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\testBIN.png";
//            new mainService().LSB_Control("r", key, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\testMES.TXT");
//            new webService().encryptMes(key, road1, road2, road3);
//            new webService().decryptMes(key, road2, road4);
//            System.out.println(new webService().evaluate(road1, road2));
//            new webService().transBin(180, road1, road2);
//            new webService().encryptPic(road1, road2, road5);
            new webService().decryptPic(road5, road2);
//            BinaryPic bin = new BinaryPic();
//            bin.setImageBin(road1, road5, road4);
//            bin.getBinFromImage(road4, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\nS6.png");
//            System.out.println(new webService().FileList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
