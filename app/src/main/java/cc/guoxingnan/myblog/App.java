package cc.guoxingnan.myblog;

import android.app.Application;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by mixinan on 2016/6/3.
 */
public class App extends Application {
    public SoundPool pool;
    private HashMap<Integer, Integer> soundMap;

    @Override
    public void onCreate() {
        super.onCreate();
        initSound();
    }

    private void initSound() {
        pool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundMap = new HashMap<Integer, Integer>();
        soundMap.put(1, pool.load(getApplicationContext(), R.raw.flush, 1));
        soundMap.put(2, pool.load(getApplicationContext(), R.raw.ad, 1));
        soundMap.put(3, pool.load(getApplicationContext(), R.raw.top, 1));
    }

    public void play_flush() {
        pool.play(soundMap.get(1), 1, 1, 0, 0, 1);
    }

    public void play_ad() {
        pool.play(soundMap.get(2), 1, 1, 0, 0, 1);
    }

    public void play_top() {
        pool.play(soundMap.get(3), 1, 1, 0, 0, 1);
    }


}
