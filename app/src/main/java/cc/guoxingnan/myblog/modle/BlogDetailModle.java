package cc.guoxingnan.myblog.modle;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cc.guoxingnan.myblog.ui.BlogDetailActivity;

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
//			System.out.println(doc);

                /**-------标题-------*/
                String title = doc.getElementsByTag("title").first().text();
                //移动客户端|万码千钧
                title = title.substring(0,title.indexOf('|'));
//			System.out.println(title);

                /**-------正文-------*/
                String text = doc.getElementsByClass("post-content").first().text();
                //本来没有换行，把“-”替换为换行符
                text = text.replaceAll("-","\n\n");
//                Log.i("Test", "text分段: " + text);

                String currentPath = doc.getElementsByClass("format-bubble").first().attr("href");

                /**-------上一篇，下一篇(标题，链接)-------*/
                String olderTitle = "";
                String olderPath = "";
                if ("http://guoxingnan.cc/the_way_to_nanjing/".equals(url)) {
                    olderTitle = "没有了";
                } else {
                    olderTitle = doc.select("a.post-nav-older").first().attr("title");
                    olderTitle = olderTitle.replaceAll("Previous post", "上一篇");
                    olderPath = doc.select("a.post-nav-older").first().attr("href");
                }


                String newerTitle = "";
                String newerPath = "";
                if (position <= 1) {
                    newerTitle = "首页";
                } else {
                    newerTitle = doc.select("a.post-nav-newer").first().attr("title");
                    newerTitle = newerTitle.replaceAll("Next post", "下一篇");
                    newerPath = doc.select("a.post-nav-newer").first().attr("href");
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
