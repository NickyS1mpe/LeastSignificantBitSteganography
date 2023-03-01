package LSB.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @ClassName: LSB.web.Application
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 17:48 2023/2/28
 **/
@SpringBootApplication
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
