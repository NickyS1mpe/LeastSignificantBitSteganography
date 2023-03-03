package LSB.web;

import LSB.web.Service.mainService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

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
            String mes = Files.readString(Paths.get("C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\testMES.TXT"));
            new mainService().LSB_Control("r", mes, "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\FILES.png", "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\static\\FILES.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
