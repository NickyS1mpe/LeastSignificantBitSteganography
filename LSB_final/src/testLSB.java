import org.junit.Test;

//测试类
public class testLSB {
    @Test
    public void testLSB() {
        String key = "asdfghjklzxcvbnm", key1 = "qwertyuiopzxcvbn";
        String road1 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB_final\\Pic\\plane.png",
                road2 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB_final\\Res\\test1.png",
                road3 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB_final\\Text\\SCNmes.TXT",
                road4 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB_final\\Res\\test1.txt",
                road5 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB_final\\Res\\bin.png",
                road6 = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB_final\\Res\\eaBIN.png";


        //文字隐写
//        new mainService().encryptMes(key1, road1, road2, road3, "MLSB", false);
//        new mainService().decryptMes(key1, road2, road4, "MLSB", false);

        //二值图像隐写
//            new mainService().encryptPic(key, road1, road2, road5, "Diff", true);
//            new mainService().decryptPic(key, road5, road2, "Diff", true);

        //边缘检测嵌入
        //嵌入文本
        new mainService().ea_Encrypt(80, 20, 1, key, road1, road2, road3);
        new mainService().ea_Decrypt(key, road2, road4);

        //嵌入二值图像
//        new mainService().ea_EncryptPic(80, 20, 1, key, road1, road2, road5);
//        new mainService().ea_DecryptPic(key, road2, road6);

        //灰度图转换
//            new mainService().getGrayImage(0, 2, road1, road6);

        //rs分析
//        new mainService().RS_Analysis(2, -0.03, road2);

        //增加椒盐噪点
//            new mainService().addNoise(0.05, road2, road5);

        //二值图像转换
//        new mainService().transBin(50, road1, road2);

        //图像比对
//        new mainService().compare(road1, road2, road6);

        //MSE||PSNR 图像质量评估
        System.out.println(new mainService().evaluate(road1, road2));

        //获取直方图
        new mainService().getHistogram(road2);
    }
}
