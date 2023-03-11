package LSB.web.Model;

/**
 * @ClassName: Logs
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 20:59 2023/3/10
 **/
public class Logs {
    String name;
    String time;
    String event;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
