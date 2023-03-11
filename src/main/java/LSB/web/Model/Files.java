package LSB.web.Model;

/**
 * @ClassName: Files
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 17:12 2023/3/10
 **/
public class Files {
    String file_name;
    String file_type;
    String file_size;
    String file_pixel;

    public String getFile_pixel() {
        return file_pixel;
    }

    public void setFile_pixel(String file_pixel) {
        this.file_pixel = file_pixel;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
