package cc.guoxingnan.myblog.modle;

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

import cc.guoxingnan.myblog.entity.Radio;
import cc.guoxingnan.myblog.entity.RadioState;
import cc.guoxingnan.myblog.entity.Video;

/**
 * Created by mixinan on 2016/6/14.
 */
public class MediaModle {
    private String TAG = "Test";
    private String URL_VIDEO = "http://v.qq.com/vplus/565058d102222715d198c58fb86eb4dc/videos";
    private String URL_RADIO = "http://www.lizhi.fm/api/radio_audios_iframe?s=0&l=20&flag=16&band=1583816";

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
                    Element e = doc.getElementsByClass("figures_list").first();

                    Elements es = e.getElementsByClass("list_item");

                    List<Video> videos = new ArrayList<Video>();

                    for (int i = 0; i < es.size(); i++) {
                        Element element = es.get(i);
                        Element a = element.getElementsByTag("a").first();

                        String url = a.attr("href");
                        String title = a.attr("title");

                        Element em = a.getElementsByTag("em").first();
                        String duration = em.text();

                        Element img = a.getElementsByTag("img").first();
                        String image = img.attr("src");

                        String playTimes = element.getElementsByClass("info_inner").first().text();

                        String publishTime = element.getElementsByClass("figure_info_time").first().text();

                        Video v = new Video(title, playTimes, publishTime, url, image, duration);

//                       Log.i(TAG,title+"\n"+url+"\n"+duration+"\n"+image+"\n"+playTimes+"\n"+publishTime+"\n");

                        videos.add(v);

                    }
                    return videos;
                } catch (IOException e) {
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
                    Element e = doc.getElementsByTag("script").first();
                    String data = e.toString();
                    String json = data.substring(data.indexOf("["), data.indexOf("]") + 1);

                    JSONArray array = new JSONArray(json);

                    List<Radio> radios = new ArrayList<Radio>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String title = obj.getString("name");
                        String url = obj.getString("url");
                        int duration = obj.getInt("duration");
                        long create_time = obj.getLong("create_time");

                        JSONObject stateObj = obj.getJSONObject("stats");
                        int playTimes = stateObj.getInt("play");

                        Radio r = new Radio();
                        r.setDuration(duration);
                        r.setCreate_time(create_time);
                        r.setUrl(url);
                        r.setName(title);
                        r.setState(new RadioState(playTimes,0));

//                        Log.i(TAG, "doInBackground: " + title + "\n" + url+"\n"+duration + "\n" + create_time + "\n" + playTimes + "\n");

                        radios.add(r);
                    }
                    return radios;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
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