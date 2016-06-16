package cc.guoxingnan.myblog.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by mixinan on 2016/6/16.
 */
public class PlayMusicService extends Service {
    private MediaPlayer player;
    private boolean isLoop = true;

    @Override
    public void onCreate() {
        player = new MediaPlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                Intent i = new Intent("ACTION_START_PLAY");
                sendBroadcast(i);
            }
        });
        new WorkThread().start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    private class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isLoop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (player.isPlaying()) {
                    Intent i = new Intent("ACTION_UPDATE_PROGRESS");
                    i.putExtra("current", player.getCurrentPosition());
                    i.putExtra("total", player.getDuration());
                    sendBroadcast(i);
                }
            }
        }
    }

    public class MusicBinder extends Binder {
        public void seekTo(int progress) {
            player.seekTo(progress);
        }

        public void playOrPause() {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }

        }

        public void playMusic(String url) {
            try {
                player.reset();
                player.setDataSource(url);
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
