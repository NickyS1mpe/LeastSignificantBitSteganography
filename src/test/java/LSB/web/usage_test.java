package LSB.web;

import LSB.web.Function.mainService;
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
            new mainService().LSB_Control("r", "testBase64", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\BASE64.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\1_BASE64.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
