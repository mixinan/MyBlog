package cc.guoxingnan.myblog.module;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.util.Constant;

/**
 * Created by mixinan on 2016/5/28.
 */
public class BlogModule {

    public void getBlogList(final Call_Back callback, final int currentPage) {

        AsyncTask<Integer, Void, List<Blog>> task = new AsyncTask<Integer, Void, List<Blog>>() {

            @Override
            protected List<Blog> doInBackground(Integer... params) {
                try {
                    ArrayList<Blog> blogs = new ArrayList<Blog>();

                    Document doc = Jsoup.connect(Constant.BASE_URL + currentPage).get();
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
}
