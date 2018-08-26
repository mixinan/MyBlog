package cc.guoxingnan.myblog.modle;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.guoxingnan.myblog.App;
import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.ui.MainActivity;
import cc.guoxingnan.myblog.util.Constant;
import cc.guoxingnan.myblog.util.ToastUtil;

/**
 * Created by mixinan on 2016/5/28.
 */
public class BlogModle {

    public void getBlogList(final Call_Back callback, final int currentPage) {

        AsyncTask<Integer, Void, List<Blog>> task = new AsyncTask<Integer, Void, List<Blog>>() {

            @Override
            protected List<Blog> doInBackground(Integer... params) {
                try {
                    ArrayList<Blog> blogs = new ArrayList<Blog>();

                    //   http://blog.2hao.cc/archives   第一页
                    //   http://blog.2hao.cc/archives/page/2/  第二页

                    String url  =Constant.BASE_URL+"/archives/";

                    if (currentPage > 1){
                        url = Constant.BASE_URL+"/archives/"+"page/"+currentPage;
                    }

                    Document doc = Jsoup.connect(url).get();

                    Elements articles = doc.getElementsByTag("article");

                    /*
                      <article class="archive-article archive-type-post">
                          <div class="archive-article-inner">
                           <header class="archive-article-header">
                            <a href="/2018/07/27/gonanjing/" class="archive-article-date"> <time datetime="2018-07-27T11:55:56.000Z" itemprop="datePublished">7月 27</time> </a>
                            <h1 itemprop="name"> <a class="archive-article-title" href="/2018/07/27/gonanjing/">返回南京</a> </h1>
                           </header>
                          </div>
                      </article>
                    */

                    for (int i = 0; i < articles.size(); i++) {

                        Element article = articles.get(i);

                        Element header = article.getElementsByTag("header").first();
                        Element a2 = header.getElementsByTag("a").get(1);
                        String path = a2.attr("href");
                        String title = a2.text();

                        Element time_element = article.getElementsByTag("time").first();
                        String time = time_element.attr("datetime");



//                        Log.i("Test", "\n地址：" + path + "\n题目：" + title + "\n时间：" + time + "\n内容：" + content);

                        Blog blog = new Blog();
                        blog.setPath(Constant.BASE_URL+path);
                        blog.setTitle(title);
                        blog.setTime(parseTime(time));
                        blog.setContent(title);

                        Log.i("jsoup", "doInBackground: "+blog.toString());

                        //添加数据到集合
                        blogs.add(blog);
                    }

                    return blogs;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Blog> blogs) {
                callback.onBlogsLoaded(blogs);
            }
        };
        task.execute();
    }


    public interface Call_Back {
        void onBlogsLoaded(List<Blog> blogs);
    }


    /**
     * 解析时间字符串
     2018-05-01T08:28:22.000Z
     解析目标：
     2018-05-01 08:28
     * @param str
     * @return
     */
    String parseTime(String str){
        Log.i("time", "parseTime: "+str);
        String time = "";
        int i = str.indexOf("T");
        time += str.substring(0,i);
        time += " "+str.substring(i+1).substring(0,5);
        return time;
    }


}
