package cc.hao2.blog.entity;

/**
 * Created by mixinan on 2016/6/14.
 */
public class Video {
    private String title;
    private String playTimes; //播放次数
    private String publishedTime; //发布时间
    private String url;
    private String image;
    private String duration;

    public Video() {
    }

    public Video(String title, String playTimes, String publishedTime, String url, String image, String duration) {
        this.title = title;
        this.playTimes = playTimes;
        this.publishedTime = publishedTime;
        this.url = url;
        this.image = image;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(String playTimes) {
        this.playTimes = playTimes;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "title：" + title;
    }
}
