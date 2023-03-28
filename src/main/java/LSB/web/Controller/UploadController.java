package LSB.web.Controller;

import LSB.web.Service.mainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: UploadController
 * @Description: upload
 * @Author: Nick Lee
 * @Date: Create in 16:35 2023/3/6
 **/
@Controller
public class UploadController {
    @Autowired
    mainService webService;

    @PostMapping(value = "/upload", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        return webService.upload(file);
    }
}
