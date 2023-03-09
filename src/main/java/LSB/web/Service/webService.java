package LSB.web.Service;

import LSB.web.Controller.UploadController;
import LSB.web.Function.mainService;
import LSB.web.Mapper.UserMapper;
import LSB.web.Model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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

    @Autowired
    private UserMapper userMapper;

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

    public String encryptPic(String key, String road1, String road2, String road3) {
        mainService ms = new mainService();
        //t1->readPic->getBinary
        CompletableFuture<Void> t1 = CompletableFuture.runAsync(() -> {
            LOGGER.info("reading picture...");
            ms.readPic(road1);
            ms.load();
        });
        //t2->encrypt mes
        CompletableFuture<Void> t2 = CompletableFuture.runAsync(() -> {
            LOGGER.info("encrypting mes...");
            ms.readTXT(road3);
            ms.Encrypt(key);
        });
        //t3->writePic
        CompletableFuture<String> t3 = t1.thenCombine(t2, (__, tf) -> {
            LOGGER.info("writing picture...");
            ms.set();
            ms.writePic(road2);
            return "Finish Encrypting";
        });
        System.out.println(t3.join());
        return "finish";
    }

    public String decryptPic(String key, String road1, String road2) {
        mainService ms = new mainService();
        LOGGER.info("reading picture...");
        ms.readPic(road1);
        ms.load();

        LOGGER.info("decrypting mes...");
        ms.Decrypt(key);

        LOGGER.info("writing text...");
        ms.writeTXT(road2);
        return "finish";
    }

    public Account findBy() {
        String name = "Ming";
        return userMapper.getDetails(name);
    }
}
