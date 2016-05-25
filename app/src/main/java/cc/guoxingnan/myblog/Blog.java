package cc.guoxingnan.myblog;

import java.io.Serializable;

/**
 * Created by mixinan on 2016/5/22.
 */
public class Blog implements Serializable{
    private String title;
    private String time;
    private String content;
    private String path;

    public Blog(String title, String time, String content, String path) {
        this.title = title;
        this.time = time;
        this.content = content;
        this.path = path;
    }

    public Blog() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
