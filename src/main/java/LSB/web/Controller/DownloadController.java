package LSB.web.Controller;

import LSB.web.Model.Preview;
import LSB.web.Service.mainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: DownloadController
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 16:57 2023/3/6
 **/
@Controller
public class DownloadController {
    @Autowired
    mainService webService;

    /**
     * @Author: Nick Lee
     * @Description: TODO 下载文件
     * @Date: 2022/3/7 22:45
     * @Return: java.lang.String
     **/
    @PostMapping("/download")
    @ResponseBody
    public String FileDownload(HttpServletResponse response, String filename) {
        return "";
    }

    /**
     * 读取txt文件的内容
     *
     * @param filename 想要读取的文件对象
     * @return 返回文件内容
     */
    @GetMapping(value = "/preview/{filename}")
    @ResponseBody
    @CrossOrigin("*")
    public Preview Get_File(@PathVariable("filename") String filename) {
        return new Preview();
    }

}
