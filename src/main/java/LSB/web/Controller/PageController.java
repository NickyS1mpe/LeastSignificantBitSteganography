package LSB.web.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: PageController
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 11:04 2023/3/3
 **/
@Controller
public class PageController {
    @RequestMapping("")
    public String index() {
        return "redirect:LSB";
    }
}
