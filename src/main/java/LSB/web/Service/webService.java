package LSB.web.Service;

import LSB.web.Controller.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName: webService
 * @Description: web service
 * @Author: Nick Lee
 * @Date: Create in 16:29 2023/3/6
 **/
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class webService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        var fileName = file.getOriginalFilename();
        String[] s = fileName.split("\\.");
        var type = s[s.length - 1].toLowerCase();
        var filePath = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload\\";
        filePath += type.equals("png") ? "pic" : "txt";
        File fs = new File(filePath + "\\" + fileName);
        try {
            file.transferTo(fs);
            LOGGER.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return "上传失败！";
    }
}
