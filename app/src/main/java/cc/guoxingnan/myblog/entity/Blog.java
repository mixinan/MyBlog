package cc.guoxingnan.myblog.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mixinan on 2016/5/22.
 */
public class Blog implements Parcelable{
    private String title;
    private String time;
    private String content;
    private String path;


    protected Blog(Parcel in) {
        title = in.readString();
        time = in.readString();
        content = in.readString();
        path = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(time);
        dest.writeString(content);
        dest.writeString(path);
    }





    
    public Blog() {
    }

    public Blog(String title, String time, String content, String path) {
        this.title = title;
        this.time = time;
        this.content = content;
        this.path = path;
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

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
