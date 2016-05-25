package cc.guoxingnan.myblog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView lv;
    private List<Blog> blogs;
    private BlogAdapter adapter;
    private static String url = "http://guoxingnan.cc";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            blogs = (List<Blog>) msg.obj;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取控件
        lv = (ListView) findViewById(R.id.lv);

        //获取数据
        blogs = new ArrayList<Blog>();
        Thread thread = new InnerThread();
        thread.start();

        //显示数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = new BlogAdapter(MainActivity.this, blogs);
                Log.w("Test", "adapter count:" + adapter.getCount());
                lv.setAdapter(adapter);
            }
        }, 2000);


        //设置监听
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(blogs.get(position).getPath()));
        startActivity(intent);
    }


    class InnerThread extends Thread {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements e1s = doc.getElementsByClass("content-inner");
                List<Blog> blogs = new ArrayList<Blog>();
                Blog blog = new Blog();
                for (int i = 0; i < e1s.size(); i++) {
                    Elements h2 = e1s.get(i).getElementsByTag("h2");
                    String path = h2.first().getElementsByTag("a").attr("href");
                    String title = h2.first().getElementsByTag("a").text();

                    Elements d1s = e1s.get(i).getElementsByClass("post-date");
                    String time = d1s.first().text();

                    Elements c1s = e1s.get(i).getElementsByClass("post-content");
                    String content = c1s.first().text();

                    Log.i("Test", "\n地址：" + path + "\n题目：" + title + "\n时间：" + time + "\n内容："+content);

                    blog.setTitle(title);
                    blog.setTime(time);
                    blog.setContent(content);
                    blog.setPath(path);

                    //添加数据到集合
                    blogs.add(blog);
                }

                //发消息
                Looper.prepare();
                Message m = new Message();
                m.obj = blogs;
                handler.sendMessage(m);

                Log.w("Test", "data.size---" + blogs.size());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
