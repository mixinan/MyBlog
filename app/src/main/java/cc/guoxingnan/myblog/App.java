package cc.guoxingnan.myblog;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.List;

import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.entity.Radio;
import cc.guoxingnan.myblog.entity.Video;
import cc.guoxingnan.myblog.modle.BlogModle;

/**
 * Created by mixinan on 2016/6/3.
 */
public class App extends Application {
    public SoundPool pool;
    private HashMap<Integer, Integer> soundMap;
    //是否开启声音
    private boolean haveSound;

    private BlogModle modle;
    private List<Blog> data;
    private List<Radio> radios;
    private List<Video> videos;


    private static App app;

    private int homePage = 1;

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        initSound();

        initData(homePage);
    }

    public void initData(int currentPage) {
<<<<<<< HEAD

        modle = new BlogModle();

        //获取博客列表，总页数，并存储数据
=======
        modle = new BlogModle();
>>>>>>> 31dc9620f58883337355084c90edf26b0a1371c9
        modle.getBlogList(new BlogModle.Call_Back() {
            @Override
            public void onBlogsLoaded(List<Blog> blogs) {
                App.this.data = blogs;
            }
<<<<<<< HEAD

        }, currentPage);

=======
        }, currentPage);
>>>>>>> 31dc9620f58883337355084c90edf26b0a1371c9
    }

    public void setData(List<Blog> data) {
        this.data = data;
    }

    public void setDataRadios(List<Radio> data) {
        this.radios = data;
    }

    public void setDataVideos(List<Video> data) {
        this.videos = data;
    }

    public List<Blog> getBlogs() {
        return data;
    }

<<<<<<< HEAD

=======
>>>>>>> 31dc9620f58883337355084c90edf26b0a1371c9
    private void initSound() {
        pool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundMap = new HashMap<Integer, Integer>();
        soundMap.put(1, pool.load(getApplicationContext(), R.raw.flush, 1));
        soundMap.put(2, pool.load(getApplicationContext(), R.raw.ad, 1));
        soundMap.put(3, pool.load(getApplicationContext(), R.raw.top, 1));
    }

    public void play_flush() {
        if (haveSound) {
            pool.play(soundMap.get(1), 1, 1, 0, 0, 1);
        } else {
            return;
        }
    }

    public void play_ad() {
        if (haveSound) {
            pool.play(soundMap.get(2), 1, 1, 0, 0, 1);
        } else {
            return;
        }
    }

    public void play_top() {
        if (haveSound) {
            pool.play(soundMap.get(3), 1, 1, 0, 0, 1);
        } else {
            return;
        }
    }

    public void openSound() {
        haveSound = true;
        saveSoundState(haveSound);
    }


    public void closeSound() {
        haveSound = false;
        saveSoundState(haveSound);
    }


    private void saveSoundState(boolean haveSound) {
        SharedPreferences preferences = getSharedPreferences("sound", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("soundState", haveSound);
        editor.commit();
    }

    public boolean getSoundState() {
        SharedPreferences preferences = getSharedPreferences("sound", MODE_PRIVATE);
        return preferences.getBoolean("soundState", false);
    }

}
