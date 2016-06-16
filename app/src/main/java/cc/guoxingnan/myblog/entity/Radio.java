package cc.guoxingnan.myblog.entity;

/**
 * Created by mixinan on 2016/6/14.
 */
public class Radio {
            private String name;
            private String url;
            private int duration;
            private long create_time;
            private RadioState state;
            private String fixedHighPlayUrl;
            private String fixedLowPlayUrl;

    public Radio() {
    }

    public Radio(String name, String url, int duration, long create_time, RadioState state, String fixedHighPlayUrl, String fixedLowPlayUrl) {
        this.name = name;
        this.url = url;
        this.duration = duration;
        this.create_time = create_time;
        this.state = state;
        this.fixedHighPlayUrl = fixedHighPlayUrl;
        this.fixedLowPlayUrl = fixedLowPlayUrl;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public RadioState getState() {
        return state;
    }

    public void setState(RadioState state) {
        this.state = state;
    }

    public String getFixedHighPlayUrl() {
        return fixedHighPlayUrl;
    }

    public void setFixedHighPlayUrl(String fixedHighPlayUrl) {
        this.fixedHighPlayUrl = fixedHighPlayUrl;
    }

    public String getFixedLowPlayUrl() {
        return fixedLowPlayUrl;
    }

    public void setFixedLowPlayUrl(String fixedLowPlayUrl) {
        this.fixedLowPlayUrl = fixedLowPlayUrl;
    }

    @Override
    public String toString() {
        return "Radio{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", duration=" + duration +
                ", create_time=" + create_time +
                ", state=" + state +
                ", fixedHighPlayUrl='" + fixedHighPlayUrl + '\'' +
                ", fixedLowPlayUrl='" + fixedLowPlayUrl + '\'' +
                '}';
    }


}
