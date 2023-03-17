package LSB.web.Service;

import LSB.web.Controller.UploadController;
import LSB.web.Function.BinaryMes;
import LSB.web.Function.BinaryPic;
import LSB.web.Function.Perform;
import LSB.web.Mapper.UserMapper;
import LSB.web.Model.Account;
import LSB.web.Model.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public String encryptMes(String key, String road1, String road2, String road3, String method) {
        BinaryMes ms = new BinaryMes();

        //t1->readPic->getBinary
        CompletableFuture<Void> t1 = CompletableFuture.runAsync(() -> {
            LOGGER.info("reading picture...");
            ms.setImage(ms.readPic(road1));
            ms.load();
        });

        //t2->encrypt mes
        CompletableFuture<Void> t2 = CompletableFuture.runAsync(() -> {
            LOGGER.info("encrypting mes...");
            ms.readTXT(road3);
            ms.Encrypt(key, true);
        });

        //t3->writePic
        CompletableFuture<String> t3 = t1.thenCombine(t2, (__, tf) -> {
            LOGGER.info("writing picture...");
            switch (method) {
                case "LSB":
                    ms.set();
                    break;
                case "MLSB":
                    ms.set_mLSB();
                    break;
                case "Diff":
                    ms.set_Diff();//set difference
                    break;
            }
            ms.writePic(ms.getImage(), road2);
            return "finish Encrypting";
        });

        System.out.println(t3.join());
        LOGGER.info("encrypt successfully");
        return "finish";
    }

    public String decryptMes(String key, String road1, String road2, String method) {
        BinaryMes ms = new BinaryMes();
        LOGGER.info("reading picture...");
        ms.setImage(ms.readPic(road1));
        ms.load();

        LOGGER.info("decrypting mes...");
        if (method.equals("LSB") || method.equals("MLSB"))
            ms.Decrypt(key, true);
        else if (method.equals("Diff"))
            ms.De_Diff(key, true);//get difference

        LOGGER.info("writing text...");
        ms.writeTXT(road2);
        LOGGER.info("decrypt successfully");
        return "finish";
    }

    public String encryptPic(String road1, String road2, String road3) {
        new BinaryPic().setImageBin(road1, road2, road3);
        return "finish";
    }

    public String decryptPic(String road1, String road2) {
        new BinaryPic().getBinFromImage(road1, road2);
        return "finish";
    }

    public String transBin(int threshold, String road1, String road2) {
        new BinaryPic().getBinPic(threshold, road1, road2);
        return "finish";
    }

    /**
     * @Author: Nick Lee
     * @Description: evaluate pics' quality
     * @Date: 2023/3/10 21:29
     * @Return:
     **/
    public String evaluate(String read1, String read2) {
        return new Perform().MSE(read1, read2) + "|" + new Perform().PSNR(read1, read2);
    }

    public Account findBy(String name) {
        return userMapper.findUserByName(name);
    }

    /**
     * @Author: Nick Lee
     * @Description: TODO
     * @Date: 2023/3/10 21:01
     * @Return:
     **/
    public List<Files> FileList(String name) {
        List<Files> files = new ArrayList<>();
        String path = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB\\src\\main\\resources\\upload";
        if (!name.equals("root"))
            path += "\\" + name;
        getFile(path, files);
        LOGGER.info("get file list successfully");
        return files;
    }

    /**
     * @Author:
     * @Description: get file list
     * @Date: 2022/3/11 22:09
     * @Return:
     **/
    private void getFile(String path, List<Files> files) {
        try {
            File file = new File(path);
            File[] array = file.listFiles();
            for (File value : array) {
                Files get_file = new Files();
                if (value.isFile())//如果是文件,只输出文件名字
                {
                    get_file.setFile_name(value.getName());
                    if (value.getName().endsWith("png")) {
                        if (value.getName().substring(0, 3).equalsIgnoreCase("bin"))
                            get_file.setFile_type("bin_pic");
                        else
                            get_file.setFile_type("pic");
                        BufferedImage bi = ImageIO.read(value);
                        get_file.setFile_pixel(bi.getHeight() + "x" + bi.getWidth());
                    } else
                        get_file.setFile_type("txt");
                    get_file.setFile_size(Long.toString(value.length()));
                    files.add(get_file);
                } else if (value.isDirectory())
                {
                    get_file.setFile_name(value.getName());
                    get_file.setFile_type("Directory");
                    files.add(get_file);
                    getFile(value.getPath(), files);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: Nick Lee
     * @Description: get current time
     * @Date: 2023/3/10 21:02
     * @Return:
     **/
    public String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public String main_encrypt(MultipartFile file1, MultipartFile file2, String key, HttpSession session) {
        return "";
    }
}
