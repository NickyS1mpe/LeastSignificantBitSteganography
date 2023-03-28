package LSB.web;

import LSB.web.Service.mainService;
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
            String key1 = "qwertyuiopzxcvbn";
            String road1 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\pic\\house.png",
                    road2 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\hT.png",
                    road3 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\txt\\CNmes.TXT",
                    road4 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resTXT\\houseTS12.TXT",
                    road5 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\testBINs.png",
                    road6 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\testBINd.png";
//            new mainService().LSB_Control("r", key, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\testMES.TXT");
//            new mainService().encryptMes(key1, road1, road2, road3, "MLSB", false);
//            new mainService().decryptMes(key1, road2, road4, "Diff", true);

//            new mainService().transBin(180, road1, road2);
//            new mainService().encryptPic(key, road1, road2, road5, "Diff", true);
//            new mainService().decryptPic(key, road5, road2, "Diff", true);
            new mainService().ea_Encrypt(60, 40, 128, key, road1, road2, road3);
//            new mainService().ea_Decrypt(key, road2, road4);
//            new mainService().transBin(60, road1, road2);
//            new mainService().decryptPic(road5, road2);
//            new mainService().encryptPic(key, road1, road2, road5, "Diff");
//            new mainService().decryptPic(key, road5, road6, "Diff");
//            new mainService().ea_Analysis(60, 40, road1, road2);
//            new mainService().compare(road1, road2, road6);
            System.out.println(new mainService().evaluate(road1, road2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
