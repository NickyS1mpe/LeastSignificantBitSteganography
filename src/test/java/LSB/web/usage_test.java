package LSB.web;

import LSB.web.Function.RS_Analyze;
import LSB.web.Service.webService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

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
            String road1 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\pic\\splash.png",
                    road2 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\lsb_tree.png",
                    road3 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\txt\\news.TXT",
                    road4 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resTXT\\stree.TXT",
                    road5 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\Ming\\resPic\\testBIN.png";
//            new mainService().LSB_Control("r", key, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\read_TXT.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\testMES.TXT");
//            new webService().encryptMes(key, road1, road2, road3, "LSB");
//            new webService().decryptMes(key, road2, road4, "LSB");
//            System.out.println(new webService().evaluate(road1, road2));
//            new webService().transBin(180, road1, road2);
//            new webService().encryptPic(road1, road2, road5);
//            new webService().decryptPic(road5, road2);
//            BinaryPic bin = new BinaryPic();
//            bin.setImageBin(road1, road5, road4);
//            bin.getBinFromImage(road4, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\nS6.png");
//            System.out.println(new webService().FileList());
            double[] res;
            double[][] rate = new double[2][4];
            RS_Analyze rs = new RS_Analyze();
            rs.judge(2, 0.015, 0.18, road1);
//            rs.setImg(road2);
//            int i = 2;
//            rate = new double[2][4];
//            res = rs.count(i, "R", false);
//            rate[0][0] += res[0];
//            rate[0][1] += res[1];
//            rate[0][2] += res[2];
//            rate[0][3] += res[3];
//            res = rs.count(i, "G", false);
//            rate[0][0] += res[0];
//            rate[0][1] += res[1];
//            rate[0][2] += res[2];
//            rate[0][3] += res[3];
//            res = rs.count(i, "B", false);
//            rate[0][0] += res[0];
//            rate[0][1] += res[1];
//            rate[0][2] += res[2];
//            rate[0][3] += res[3];
//            res = rs.count(i, "R", true);
//            rate[1][0] += res[0];
//            rate[1][1] += res[1];
//            rate[1][2] += res[2];
//            rate[1][3] += res[3];
//            res = rs.count(i, "G", true);
//            rate[1][0] += res[0];
//            rate[1][1] += res[1];
//            rate[1][2] += res[2];
//            rate[1][3] += res[3];
//            res = rs.count(i, "B", true);
//            rate[1][0] += res[0];
//            rate[1][1] += res[1];
//            rate[1][2] += res[2];
//            rate[1][3] += res[3];
//            double rate1 = rs.countRate(rate);
//            rs.setImg(road1);
//            rate = new double[2][4];
//            res = rs.count(i, "R", false);
//            rate[0][0] += res[0];
//            rate[0][1] += res[1];
//            rate[0][2] += res[2];
//            rate[0][3] += res[3];
//            res = rs.count(i, "G", false);
//            rate[0][0] += res[0];
//            rate[0][1] += res[1];
//            rate[0][2] += res[2];
//            rate[0][3] += res[3];
//            res = rs.count(i, "B", false);
//            rate[0][0] += res[0];
//            rate[0][1] += res[1];
//            rate[0][2] += res[2];
//            rate[0][3] += res[3];
//            res = rs.count(i, "R", true);
//            rate[1][0] += res[0];
//            rate[1][1] += res[1];
//            rate[1][2] += res[2];
//            rate[1][3] += res[3];
//            res = rs.count(i, "G", true);
//            rate[1][0] += res[0];
//            rate[1][1] += res[1];
//            rate[1][2] += res[2];
//            rate[1][3] += res[3];
//            res = rs.count(i, "B", true);
//            rate[1][0] += res[0];
//            rate[1][1] += res[1];
//            rate[1][2] += res[2];
//            rate[1][3] += res[3];
//            double rate2 = rs.countRate(rate);
//            System.out.println((rate1 - rate2) / (1 - rate2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
