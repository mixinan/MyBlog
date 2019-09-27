package cc.hao2.blog.entity;

/**
 * Created by mixinan on 2016/6/14.
 */
public class Radio {
            private String name;
            private String url;
            private String duration;
            private String create_time;

    public Radio() {
    }

    public Radio(String name, String url, String duration, String create_time) {
        this.name = name;
        this.url = url;
        this.duration = duration;
        this.create_time = create_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    @Override
    public String toString() {
        return "Radio{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", duration=" + duration +
                ", create_time=" + create_time +'}';
    }


}
