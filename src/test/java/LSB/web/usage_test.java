package LSB.web;

import LSB.web.Function.BinaryMes;
import LSB.web.Function.BinaryPic;
import LSB.web.Function.EdgeAdaptive;
import LSB.web.Function.RS_Analyze;
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
            String road1 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\pic\\house.png",
                    road2 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\BIN_houseR.png",
                    road3 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\txt\\news.TXT",
                    road4 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resTXT\\houseTS1.TXT",
                    road5 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\testBIN1.png",
                    road6 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\testBINx.png";
//            new mainService().LSB_Control("r", key, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\testMES.TXT");
//            new webService().encryptMes(key, road1, road2, road3, "MLSB");
//            new webService().decryptMes(key, road2, road4, "MLSB");

//            new webService().transBin(180, road1, road2);
//            new webService().encryptPic(road1, road2, road5);
//            new webService().decryptPic(road5, road2);
//            new webService().ea_Encrypt(60, 20, key, road1, road2, road3);
//            new webService().ea_Decrypt(key, road2, road4);
//            System.out.println(new webService().evaluate(road1, road2));
//            new webService().transBin(180, road1, road2);
//            new webService().decryptPic(road5, road2);
//            new webService().encryptPic(key, road1, road2, road5, "Diff");
//            new webService().decryptPic(key, road5, road6, "Diff");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
