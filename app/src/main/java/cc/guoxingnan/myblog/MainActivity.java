package cc.guoxingnan.myblog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayList<Blog> blogs;
    private BlogAdapter adapter;
    private static String url = "http://guoxingnan.cc";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            blogs = msg.getData().getParcelableArrayList("data");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取控件
        lv = (ListView) findViewById(R.id.lv);
        blogs = new ArrayList<Blog>();

        //获取数据
        Thread thread = new InnerThread();
        thread.start();


        //显示数据
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = new BlogAdapter(MainActivity.this, blogs);

                Log.w("Test", "adapter count:" + adapter.getCount());

                lv.setAdapter(adapter);
            }
        }, 3000);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BlogDetailActivity.class);
                intent.putExtra("url", blogs.get(position).getPath());
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }


    class InnerThread extends Thread {
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
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data", blogs);
                m.setData(bundle);
                handler.sendMessage(m);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
