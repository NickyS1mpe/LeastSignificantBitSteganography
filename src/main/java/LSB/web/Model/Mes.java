package LSB.web.Model;

/**
 * @ClassName: message
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 22:36 2023/3/6
 **/
public class Mes {
    String mes;
    int[] binary;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int[] getBinary() {
        return binary;
    }

    public void setBinary(int[] binary) {
        this.binary = binary;
    }
}
