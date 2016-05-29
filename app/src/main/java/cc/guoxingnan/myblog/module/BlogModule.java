package cc.guoxingnan.myblog.module;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.ui.MainActivity;

/**
 * Created by mixinan on 2016/5/28.
 */
public class BlogModule {
    private static String url = "http://guoxingnan.cc";
    private Context context;
    private ArrayList<Blog> blogs;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    blogs = (ArrayList<Blog>) msg.obj;
                    Log.i("Test", "getBlogsMain: blogs.size---" + blogs.size());
                    MainActivity activity = (MainActivity)context;
                    activity.setMyAdapter(blogs);
                    break;
            }
        }
    };

    /**
     * 构造方法，传入调用者的上下文对象为参数，便于回调
     * 在构造方法内，开启工作线程，去得到数据集合
     * @param context
     */
    public BlogModule(Context context) {
        this.context = context;
        //获取数据
        Thread thread = new GetMainBlogsThread();
        thread.start();
    }


    /**
     * 工作线程，解析得到数据集合，并以消息形式发送到主线程
     */
    class GetMainBlogsThread extends Thread {
        @Override
        public void run() {
            try {
                ArrayList<Blog> blogs = new ArrayList<Blog>();

                Document doc = Jsoup.connect(url).get();
                Elements e1s = doc.getElementsByClass("content-inner");

                for (int i = 0; i < e1s.size(); i++) {
                    Elements h2 = e1s.get(i).getElementsByTag("h2");
                    String path = h2.first().getElementsByTag("a").attr("href");
                    String title = h2.first().getElementsByTag("a").text();

                    Elements d1s = e1s.get(i).getElementsByClass("post-date");
                    String time = d1s.first().text();

                    Elements c1s = e1s.get(i).getElementsByClass("post-content");
                    String content = c1s.first().text();

                    Log.i("Test", "\n地址：" + path + "\n题目：" + title + "\n时间：" + time + "\n内容：" + content);

                    Blog blog = new Blog();
                    blog.setTitle(title);
                    blog.setTime(time);
                    blog.setContent(content);
                    blog.setPath(path);

                    //添加数据到集合
                    blogs.add(blog);
                }

                Log.w("Test", "data.size---" + blogs.size());

                //发消息
                Message m = Message.obtain();
                m.what = 1;
                m.obj = blogs;
                handler.sendMessage(m);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
