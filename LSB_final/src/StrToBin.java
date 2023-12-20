//二进制编码程序
public class StrToBin extends Crypto {
    String message;
    int[] binary;

    public StrToBin(String message) {
        this.message = message;
    }

    //字符串转化为8位二进制数据流
    public void Encode() {
        int[] binary = new int[message.length() * 8];
        try {
            for (int i = 0; i < message.length(); i++) {
                binary[8 * i] = message.charAt(i) & 0x1;
                binary[8 * i + 1] = message.charAt(i) >> 1 & 0x1;
                binary[8 * i + 2] = message.charAt(i) >> 2 & 0x1;
                binary[8 * i + 3] = message.charAt(i) >> 3 & 0x1;
                binary[8 * i + 4] = message.charAt(i) >> 4 & 0x1;
                binary[8 * i + 5] = message.charAt(i) >> 5 & 0x1;
                binary[8 * i + 6] = message.charAt(i) >> 6 & 0x1;
                binary[8 * i + 7] = message.charAt(i) >> 7 & 0x1;
            }
            setBinary(binary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getBinary() {
        return binary;
    }

    public void setBinary(int[] binary) {
        this.binary = binary;
    }
}
