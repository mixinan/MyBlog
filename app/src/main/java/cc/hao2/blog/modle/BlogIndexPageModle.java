package cc.hao2.blog.modle;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.hao2.blog.entity.Blog;
import cc.hao2.blog.util.Constant;
import cc.hao2.blog.util.LogUtil;


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
                    //http://blog.2hao.cc/   BASE_URL（第一页）
                    //http://blog.2hao.cc/page/2/  第二页

                    String url  = Constant.BASE_URL;

                    if (currentPage > 1){
                        url = url + "/page/" + currentPage;
                    }

                    Document doc = Jsoup.connect(url+"/").get();

                    Elements articles = doc.select(".articles > .article");

//                    LogUtil.i("index", "articles: "+articles);

                    for (int i = 0; i < articles.size(); i++) {

                        Element article = articles.get(i);

                        Element a = article.select(".article-title").first();

                        // 得到“文章的标题”
                        String title = a.text();

                        // 得到“文章的相对路径地址”
                        String path = a.attr("href");

                        // 拼接出“文章的完整路径”
                        path = Constant.BASE_URL + path;

                        // 得到“文章发布的时间”
                        String time =  article.getElementsByTag("time").first().text();

                        // 得到“文章的摘要”
                        String content = article.select(".article-entry > p").first().text();

//                        LogUtil.i("Test", "\n地址：" + path + "\n题目：" + title + "\n时间：" + time + "\n内容：" + content);

                        Blog blog = new Blog();
                        blog.setPath(path);
                        blog.setTitle(title);
                        blog.setTime(time);
                        blog.setContent(content);

//                        LogUtil.i("jsoup", "doInBackground: "+blog.toString());

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
/*    String parseTime(String str){
        Log.i("time", "parseTime: "+str);
        String time = "";
        int i = str.indexOf("T");
        time += str.substring(0,i);
        time += " "+str.substring(i+1).substring(0,5);
        return time;
    }*/


}

