package LSB.web.Controller;

import LSB.web.Model.Account;
import LSB.web.Model.Files;
import LSB.web.Service.webService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 16:25 2023/3/7
 **/
@RestController
//@CrossOrigin(value = "http://localhost:7070", allowCredentials = "true")
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
    @GetMapping(value = "/api/getFiles/{name}")
    public List<Files> Files(@PathVariable("name") String name) {
        return webService.FileList(name);
    }

    @PostMapping(value = "/api/verify")
    public String Verify(String name, String password, HttpSession session) {
        Account account = webService.findBy(name);
        if (account.getPassword().equals(password)) {
            System.out.println("Login Successfully");
            System.out.println(session.getId());
            session.setAttribute("name", name);
            return "TRUE";
        }
        return "FALSE";
    }

    @PostMapping("/api/set")
    public String set(HttpSession session) {
        System.out.println("Login Successfully");
        System.out.println(session.getId());
        session.setAttribute("name", "Zhang");
        return "set";
    }

    @PostMapping("/api/get")
    public String get(HttpSession session) {
        System.out.println("Get info");
        System.out.println(session.getId());
        System.out.println(session.getAttribute("name"));
        return "get";
    }
}
