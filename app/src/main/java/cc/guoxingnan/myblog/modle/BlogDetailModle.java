package cc.guoxingnan.myblog.modle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import cc.guoxingnan.myblog.ui.BlogDetailActivity;
import cc.guoxingnan.myblog.util.Constant;

/**
 * Created by mixinan on 2016/5/28.
 */
public class BlogDetailModle {
    private Context context;
    private String url;
    private int position;
    private String[] data;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    data = (String[]) msg.obj;
                    BlogDetailActivity activity = (BlogDetailActivity) context;
                    activity.showData(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
                    break;
            }
        }
    };


    public BlogDetailModle(Context context, String url, int position) {
        this.position = position;
        this.context = context;
        this.url = url;

        new InnerThread().start();
    }


    private class InnerThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Document doc = Jsoup.connect(url).get();

                /**-------标题-------*/
                String title = doc.title();

                Log.i("detail", "title: "+title);

                /**-------正文-------*/
                String text = doc.getElementsByClass("article-entry").first().html();

                Element path_element = doc.select("meta[property]").get(2);
                String currentPath = path_element.attr("content");

                Log.i("detail", "path: " + currentPath);



                /**-------上一篇，下一篇(标题，链接)-------*/
                Elements bts = doc.getElementById("article-nav").getElementsByTag("a");

                String olderTitle = "";
                String olderPath = "";
                if ("http://blog.2hao.cc/2014/11/08/weibo-old/".equals(url)) {
                    olderTitle = "没有了";
                } else {
                    olderTitle = bts.last().text();
                    olderPath = Constant.BASE_URL + bts.last().attr("href");

                    Log.i("detail", "older: " + olderPath + " " + olderTitle);
                }


                String newerTitle = "";
                String newerPath = "";
                if (position <= 1) {
                    newerTitle = "首页";
                } else {
                    newerTitle = bts.first().text();
                    newerPath = Constant.BASE_URL + bts.first().attr("href");

                    Log.i("detail", "newer: " + newerPath + " " + newerTitle);
                }



                String[] data = {title, text, newerTitle, newerPath, olderTitle, olderPath, currentPath};

                Message message = Message.obtain();
                message.what = 2;
                message.obj = data;
                handler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
