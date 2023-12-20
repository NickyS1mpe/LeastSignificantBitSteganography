import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//获取图片的直方图
//调用同目录下的histogram.py文件运行
public class Histogram {

    //输入参数：要生成直方图的图片的路径，生成的直方图的名称
    public void getHistogram(String path, String name) {
        try {
            //要运行的python文件位置，python环境目录位置
            String command = "C:\\Users\\Nick Lee\\IdeaProjects\\LSB_final\\src\\histogram.py",
                    python_path = "D:\\BaiduNetdiskDownload\\python-3.7\\python.exe";

            //配置参数
            String[] args = {python_path, command, path, name};
            Process process = Runtime.getRuntime().exec(args);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
