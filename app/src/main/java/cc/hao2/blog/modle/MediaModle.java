package cc.hao2.blog.modle;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.hao2.blog.entity.Radio;
import cc.hao2.blog.entity.RadioState;
import cc.hao2.blog.entity.Video;
import cc.hao2.blog.util.Constant;
import cc.hao2.blog.util.LogUtil;
import cc.hao2.blog.util.NumberUtil;

/**
 * Created by mixinan on 2016/6/14.
 */
public class MediaModle {
    private String TAG = "media";
    private String URL_VIDEO = Constant.BASE_URL_VD1;
    private String URL_RADIO = Constant.BASE_URL_FM2;

    /**
     * 加载视频列表
     *
     * @param callBack
     */
    public void getVideoList(final VideoCallBack callBack) {
        AsyncTask<String, Void, List<Video>> task = new AsyncTask<String, Void, List<Video>>() {

            @Override
            protected List<Video> doInBackground(String... params) {
                try {
                    Document doc = Jsoup.connect(URL_VIDEO).get();

                    // 视频标签的列表
                    Elements items = doc.select("div.mod_content_inner>div.mod_video_list>div#_videolist_latest>div");

                    List<Video> videos = new ArrayList<Video>();

                    for (int i = 0; i < items.size(); i++) {
                        Element item = items.get(i);

                        // item里的第一个小布局
                        // 可以得到信息：视频地址、标题、时长、图片地址
                        Element a = item.child(0);

                        String url = a.attr("href");
                        String title = a.attr("title");

                        String image = a.child(0).attr("src");

                        String duration = a.child(1).text();

                        // item里的第二个小布局
                        // 可以得到信息：播放次数、发布时间
                        Element detail = item.child(1);

                        String playTimes = detail.child(1).text();

                        String publishTime = detail.child(2).text();

                        // 把所有结果，保存到Video对象
                        Video v = new Video(title, playTimes, publishTime, url, image, duration);

                       LogUtil.i(TAG,title+"\n"+url+"\n"+duration+"\n"+image+"\n"+playTimes+"\n"+publishTime+"\n");

                        videos.add(v);
                    }
                    return videos;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Video> videos) {
                super.onPostExecute(videos);
                callBack.onVideoListLoaded(videos);
            }
        };

        task.execute();
    }


    /**
     * 加载音频列表
     *
     * @param callBack
     */
    public void getRadioList(final RadioCallBack callBack) {
        AsyncTask<String, Void, List<Radio>> task = new AsyncTask<String, Void, List<Radio>>() {

            @Override
            protected List<Radio> doInBackground(String... params) {
                try {
                    Document doc = Jsoup.connect(URL_RADIO).get();

                    // 声音列表
                    Element e = doc.getElementsByClass("audioList").get(2);

                    Elements as = e.getElementsByTag("a");

                    List<Radio> radios = new ArrayList<Radio>();
                    for (int i = 0; i < as.size(); i++) {
                        Element a = as.get(i);

                        String title = a.getElementsByClass(".audioName").first().text();

                        String audio_id = a.attr("data-id");

                        String duration = a.attr("data-duration");

                        // 发布时间
                        // 2019-09-03&nbsp;&nbsp;&nbsp;0评论

                        String audio_time = a.getElementsByClass("aduioTime").first().text();
                        audio_time = audio_time.substring(0, 10);
                        audio_time = audio_time.replace('-', '/');


                        // url 样式
                        // http://cdn5.lizhi.fm/audio/2019/04/10/2730914938983435782_hd.mp3
                        // "http://cdn5.lizhi.fm/audio/" + 发布时间 + "/" + audio_id + "_hd.mp3"

                        String mp3_url = "http://cdn5.lizhi.fm/audio/" + audio_time + "/" + audio_id + "_hd.mp3";

                        String playTimes = "0";

                        Radio r = new Radio();
                        r.setDuration(duration);
                        r.setCreate_time(audio_time);
                        r.setUrl(mp3_url);
                        r.setName(title);

                        LogUtil.i(TAG, "doInBackground: " + title + "\n" + mp3_url+"\n"+duration + "\n" + audio_id + "\n" + audio_time + "\n");

                        radios.add(r);
                    }

                    return radios;
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Radio> radios) {
                super.onPostExecute(radios);
                callBack.onRadioListLoaded(radios);
            }
        };
        task.execute();
    }


    public interface VideoCallBack {
        void onVideoListLoaded(List<Video> videos);
    }

    public interface RadioCallBack {
        void onRadioListLoaded(List<Radio> radios);
    }


}