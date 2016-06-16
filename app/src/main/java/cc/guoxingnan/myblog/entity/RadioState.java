package cc.guoxingnan.myblog.entity;

/**
 * Created by mixinan on 2016/6/14.
 */
public class RadioState {
    private int play;
    private int download;

    public RadioState() {
    }

    public RadioState(int play, int download) {
        this.play = play;
        this.download = download;
    }

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    @Override
    public String toString() {
        return "State{" +
                "play=" + play +
                ", download=" + download +
                '}';
    }
}
