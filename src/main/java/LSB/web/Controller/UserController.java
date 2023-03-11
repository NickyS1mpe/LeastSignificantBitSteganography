package LSB.web.Controller;

import LSB.web.Model.Account;
import LSB.web.Model.Files;
import LSB.web.Service.webService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 16:25 2023/3/7
 **/
@RestController
public class UserController {

    @Autowired
    webService webService;

    @GetMapping("/api/account/{name}")
    @ResponseBody
    public Account getInfo(@PathVariable("name") String name) {
        return webService.findBy(name);
    }

    /**
     * @Author: Nick Lee
     * @Description: get user's file list
     * @Date: 2023/3/10 20:40
     * @Return:
     **/
    @GetMapping(value = "/getFiles/{name}")
    public List<Files> Files(@PathVariable("name") String name) {
        return webService.FileList(name);
    }
}
