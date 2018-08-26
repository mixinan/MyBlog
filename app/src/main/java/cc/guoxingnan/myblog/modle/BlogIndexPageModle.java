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

import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.util.Constant;

import cc.guoxingnan.myblog.App;
import cc.guoxingnan.myblog.ui.MainActivity;
import cc.guoxingnan.myblog.util.ToastUtil;

/**
 * Created by Administrator on 2018/8/26.
 */

public class BlogIndexPageModle {

    public void getBlogList(final Call_Back callback, final int currentPage) {

        AsyncTask<Integer, Void, List<Blog>> task = new AsyncTask<Integer, Void, List<Blog>>() {

            @Override
            protected List<Blog> doInBackground(Integer... params) {
                try {
                    ArrayList<Blog> blogs = new ArrayList<Blog>();
                    //http://blog.2hao.cc/   第一页
                    //http://blog.2hao.cc/page/2/  第二页

                    String url  = Constant.BASE_URL;

                    if (currentPage > 1){
                        url = Constant.BASE_URL+"/page/"+currentPage;
                    }

                    Document doc = Jsoup.connect(url).get();

                    Elements articles = doc.getElementsByClass("article-inner");

//                    Log.i("index", "articles: "+articles);

                    for (int i = 0; i < articles.size(); i++) {

                        Element article = articles.get(i);

                        Element a = article.getElementsByClass("article-title").first();

                        String path = a.attr("href");
                        path = Constant.BASE_URL + path;

                        String title = a.text();

                        Element time_element = article.getElementsByTag("time").first();
                        String time = time_element.attr("datetime");
                        time = parseTime(time);

                        String content = article.getElementsByTag("div").last().getElementsByTag("p").first().text();

                        //Log.i("Test", "\n地址：" + path + "\n题目：" + title + "\n时间：" + time + "\n内容：" + content);

                        Blog blog = new Blog();
                        blog.setPath(path);
                        blog.setTitle(title);
                        blog.setTime(time);
                        blog.setContent(content);

                        //Log.i("jsoup", "doInBackground: "+blog.toString());

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

