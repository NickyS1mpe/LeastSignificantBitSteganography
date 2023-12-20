import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

//主要服务控制程序
public class mainService {

    //文本消息隐写
    //输入参数依次为：密钥、原始图片读取路径、加密后图片写入路径、文本读取路径、隐写方法、是否采用伪随机序列
    //密钥长度必须为16位
    //密钥将用于文本的加密和伪随机序列的生成
    public String encryptMes(String key, String path1, String path2, String path3, String method, boolean random) {
        BinaryMes ms = new BinaryMes();

        //t1->readPic->getBinary
        //读取图片，并获取图片最低位LSB
        CompletableFuture<Void> t1 = CompletableFuture.runAsync(() -> {
            System.out.println("reading picture...");
            ms.setImage(ms.readPic(path1));
            ms.load(null, false);
        });

        //t2->encrypt mes
        //读取文本，并对文本进行加密
        CompletableFuture<Void> t2 = CompletableFuture.runAsync(() -> {
            System.out.println("encrypting mes...");
            ms.readTXT(path3);
            ms.Encrypt(key, true);
        });

        //t3->writePic
        //将文本按照选择的隐写方法进行加密
        CompletableFuture<String> t3 = t1.thenCombine(t2, (__, tf) -> {
            System.out.println("writing picture...");
            switch (method) {
                //LSB替换隐写
                case "LSB":
                    ms.set(key, random);
                    break;
                //LSB匹配隐写
                case "MLSB":
                    ms.set_mLSB(key, random);
                    break;
                //LSB差分隐写
                case "Diff":
                    ms.set_Diff(key, random);
                    break;
            }
            ms.writePic(ms.getImage(), path2);
            return "finish Encrypting";
        });

        System.out.println(t3.join());
        System.out.println("encrypt successfully");
        return "finish";
    }

    //文本消息提取
    //输入参数依次为：密钥、含密图片读取路径、解密后文本写入路径、隐写方法、是否采用伪随机序列
    //密钥长度必须为16位
    //密钥将用于文本的解密和伪随机序列的生成
    public String decryptMes(String key, String path1, String path2, String method, boolean random) {
        BinaryMes ms = new BinaryMes();
        System.out.println("reading picture...");

        //读取图片
        ms.setImage(ms.readPic(path1));
        System.out.println("decrypting mes...");

        //LSB替换隐写或LSB匹配隐写
        if (method.equals("LSB") || method.equals("MLSB")) {
            ms.load(key, random);
            ms.Decrypt(key, true);
        }

        //LSB差分隐写
        else if (method.equals("Diff")) {
            ms.load(null, false);
            ms.De_Diff(key, true, random);//get difference
        }

        //文本写入
        System.out.println("writing text...");
        ms.writeTXT(path2);
        System.out.println("decrypt successfully");
        return "finish";
    }


    //二值图片隐写
    //输入参数依次为：密钥、原始图片读取路径、加密后图片写入路径、二值图像读取路径、隐写方法、是否采用伪随机序列
    //密钥长度必须为16位
    //密钥将用于信息的加密和伪随机序列的生成
    //考虑到嵌入数据量的问题，二值图像是最佳方案，灰度图像与彩色图像嵌入对空间需求量过大
    public String encryptPic(String key, String path1, String path2, String path3, String method, boolean random) {
        BinaryPic bp = new BinaryPic();
        String type = "encrypt";

        switch (method) {
            //直接嵌入图像最低位
            case "insert":
                bp.setImageBin(path1, path2, path3);
                break;

            //转化为数据流嵌入
            //LSB替换嵌入
            case "LSB":
                bp.LSB(key, path1, path2, path3, type, random);
                break;
            //LSB匹配嵌入
            case "MLSB":
                bp.MLSB(key, path1, path2, path3, type, random);
                break;
            //LSB差分嵌入
            case "Diff":
                bp.Diff(key, path1, path2, path3, type, random);
                break;
        }

        System.out.println("encrypt successfully");
        return "finish";
    }

    //二值图像提取
    //输入参数依次为：密钥、含密图片读取路径、解密后图像写入路径、隐写方法、是否采用伪随机序列
    //密钥长度必须为16位
    //密钥将用于信息的解密和伪随机序列的生成
    public String decryptPic(String key, String path1, String path2, String method, boolean random) {
        BinaryPic bp = new BinaryPic();
        String type = "decrypt";

        switch (method) {
            //直接提取图像最低位
            case "insert":
                bp.getBinFromImage(path1, path2);
                break;

            //转化为数据流提取
            //LSB替换嵌入
            case "LSB":
                bp.LSB(key, path1, "", path2, type, random);
                break;
            //LSB匹配嵌入
            case "MLSB":
                bp.MLSB(key, path1, "", path2, type, random);
                break;
            //LSB差分嵌入
            case "Diff":
                bp.Diff(key, path1, "", path2, type, random);
                break;
        }

        System.out.println("decrypt successfully");
        return "finish";
    }

    //二值图像转换
    //输入参数依次为：阈值，图片读取路径，图片写入路径
    //阈值随图片特性决定，用于Sobel边缘检测
    //分组大小默认采取边长为3个像素
    public String transBin(int threshold, String path1, String path2) {
        new BinaryPic().getBinPic(threshold, path1, path2);

        System.out.println("transform successfully");
        return "finish";
    }


    //MSE和PSNR性能评估
    //输出为MSE值|PSNR值
    public String evaluate(String path1, String path2) {
        Perform perf = new Perform();
        System.out.println("evaluated result:");

        return perf.MSE(path1, path2) + "|" + new Perform().PSNR(path1, path2);
    }

    //图像比对
    //生成二值图像，白色的像素标明图像之间存在差异，反之黑色则不存在
    //输入参数依次为：图像1读取路径、图像2读取路径、图像写入路径
    public String compare(String path1, String path2, String write) {
        Perform perf = new Perform();
        System.out.println("compared result:");

        perf.compare(path1, path2, write);

        return "finish";
    }

    //RS隐写分析检测
    //输入参数依次为：分组边长大小、初始偏差值、图片读取路径
    //R_m和R_-m间允许的偏差大小阈值为5%
    //初始偏差值用于纠正分析结果
    //分组大小n一般取2-8之间
    public String RS_Analysis(int n, double ml0, String path) {
        RS_Analyze rs = new RS_Analyze();
        System.out.println("analyzed result:");

        rs.judge(n, 0.05, ml0, path);

        System.out.println("analyze successfully");
        return "finish";
    }

    //图像边缘分析，采用Sobel边缘检测
    //输入的参数依次为：上阈值、下阈值、图片读取路径、图片写入路径
    //分别对R、G、B三通道进行边缘检测，用于观测阈值取值不同的检测结果
    //分组大小默认采取边长为3个像素
    public String ea_Analysis(int threshold1, int threshold2, String path1, String path2) {
        EdgeAdaptive ea = new EdgeAdaptive();

        ea.find(3, threshold1, threshold2, path1, path2);

        System.out.println("analyze successfully");
        return "finish";
    }

    //基于图像边缘的文本信息隐写，采用Sobel边缘检测
    //输入的参数依次为：上阈值、下阈值、随机序列偏移值、密钥、图片读取路径、图片写入路径、文本读取路径
    //分组大小默认采取边长为3个像素
    //密钥用于对文本信息进行加密
    //加密信息存储与R通道中，对G和B通道采用多位LSB嵌入
    public String ea_Encrypt(int threshold1, int threshold2, int deviation, String key, String path1, String path2, String path3) {
        EdgeAdaptive ea = new EdgeAdaptive();
        BinaryMes ms = new BinaryMes();

        ms.readTXT(path3);
        ms.Encrypt(key, true);
        ea.insert(3, threshold1, threshold2, deviation, ms.getBin(), path1, path2);

        System.out.println("encrypt successfully");
        return "finish";
    }

    //基于图像边缘的文本信息提取
    //输入的参数依次为：密钥、图片读取路径、文本写入路径
    //分组大小默认采取边长为3个像素
    //密钥用于对文本信息进行解密
    public String ea_Decrypt(String key, String path1, String road2) {
        EdgeAdaptive ea = new EdgeAdaptive();
        BinaryMes ms = new BinaryMes();

        ms.Decrypt(ea.get(path1), key, true);
        ms.writeTXT(road2);

        System.out.println("decrypt successfully");
        return "finish";
    }

    //基于图像边缘的二值图像隐写，采用Sobel边缘检测
    //输入的参数依次为：上阈值、下阈值、随机序列偏移值、密钥、图片读取路径、图片写入路径、二值图像读取路径
    //分组大小默认采取边长为3个像素
    //密钥用于对信息进行加密
    //加密信息存储与R通道中，对G和B通道采用多位LSB嵌入
    public String ea_EncryptPic(int threshold1, int threshold2, int deviation, String key, String path1, String path2, String path3) {
        EdgeAdaptive ea = new EdgeAdaptive();
        BinaryPic bp = new BinaryPic();

        ea.insert(3, threshold1, threshold2, deviation, bp.get_bin_Binary(key, path3), path1, path2);

        System.out.println("encrypt successfully");
        return "finish";
    }

    //基于图像边缘的二值图像提取
    //输入的参数依次为：密钥、图片读取路径、二值图像写入路径
    //分组大小默认采取边长为3个像素
    //密钥用于对信息进行解密
    public String ea_DecryptPic(String key, String path1, String path2) {
        EdgeAdaptive ea = new EdgeAdaptive();
        BinaryPic bp = new BinaryPic();

        bp.set_bin_Binary(ea.get(path1), key, path2);

        System.out.println("decrypt successfully");
        return "finish";
    }

    //获取灰度图
    //输入参数依次为：要处理的图像位、处理类型、图像读取路径、图像写入路径
    //处理类型type==0为获取图像灰度图，type==1为获取图像指定位的灰度图，type==2为去掉图像指定为的灰度图
    public String getGrayImage(int layer, int type, String path1, String path2) {
        BinaryPic bp = new BinaryPic();

        bp.getGrayPic(layer, type, path1, path2);

        System.out.println("transform successfully");
        return "finish";
    }


    //增加椒盐噪声
    //输入参数依次为；噪声生成概率、图像读取路径、图像写入路径
    public String addNoise(double probability, String read, String write) {
        BinaryPic bp = new BinaryPic();

        bp.addNoise(probability, read, write);

        System.out.println("add successfully");
        return "finish";
    }

    //获取时间
    //格式为年-月-日 时：分：秒
    public String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    //获取图片直方图，默认生成的直方图文件名为原图文件名改写
    //输入参数为：图像读取路径
    public String getHistogram(String path) {
        String[] r = path.split("\\\\");
        var name = r[r.length - 1].split("\\.")[0] + "Hist";
        Histogram hist = new Histogram();

        hist.getHistogram(path, name);

        System.out.println("generate successfully");
        return "finish";
    }
}
