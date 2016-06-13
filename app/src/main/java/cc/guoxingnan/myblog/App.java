package cc.guoxingnan.myblog;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by mixinan on 2016/6/3.
 */
public class App extends Application {
    public SoundPool pool;
    private HashMap<Integer, Integer> soundMap;
    //是否开启声音
    private boolean haveSound;

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

    public void openSound(){
        haveSound = true;
        saveSoundState(haveSound);
    }


    public void closeSound(){
        haveSound = false;
        saveSoundState(haveSound);
    }


    private void saveSoundState(boolean haveSound) {
        SharedPreferences preferences = getSharedPreferences("sound",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("soundState", haveSound);
        editor.commit();
    }

    public boolean getSoundState() {
        SharedPreferences preferences = getSharedPreferences("sound", MODE_PRIVATE);
        return preferences.getBoolean("soundState",false);
    }

}
