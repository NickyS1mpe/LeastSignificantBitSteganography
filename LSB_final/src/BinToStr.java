//二进制解码程序
public class BinToStr extends Crypto {
    String mes;
    int[] binary;

    public BinToStr(int[] binary) {
        this.binary = binary;
    }

    //8位二进制流转换为字符
    public void Decode() {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < binary.length / 8; i++) {
                char c = 0;
                if (binary[8 * i] == 1) c |= 0x1;
                if (binary[8 * i + 1] == 1) c |= 0x2;
                if (binary[8 * i + 2] == 1) c |= 0x4;
                if (binary[8 * i + 3] == 1) c |= 0x8;
                if (binary[8 * i + 4] == 1) c |= 0x10;
                if (binary[8 * i + 5] == 1) c |= 0x20;
                if (binary[8 * i + 6] == 1) c |= 0x40;
                if (binary[8 * i + 7] == 1) c |= 0x80;
                sb.append(c);
            }
            setMes(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
