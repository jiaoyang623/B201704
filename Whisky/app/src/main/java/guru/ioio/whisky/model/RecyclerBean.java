package guru.ioio.whisky.model;

/**
 * Created by jiaoyang on 19/06/2017.
 */

public class RecyclerBean {
    public String title;
    public String desc;
    public String time;
    public int position;

    public RecyclerBean(String title, String desc, String time) {
        this.title = title;
        this.desc = desc;
        this.time = time;
    }
}
