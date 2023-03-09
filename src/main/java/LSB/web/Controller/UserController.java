package LSB.web.Controller;

import LSB.web.Model.Account;
import LSB.web.Service.webService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: UserController
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 16:25 2023/3/7
 **/
@RestController
public class UserController {

    webService webService;

    @GetMapping("/api/account/")
    @ResponseBody
    public Account getInfo() {
        return webService.findBy();
    }
}
