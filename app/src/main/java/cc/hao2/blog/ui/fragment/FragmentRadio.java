package cc.hao2.blog.ui.fragment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import cc.hao2.blog.R;
import cc.hao2.blog.adapter.RadioAdapter;
import cc.hao2.blog.entity.Radio;
import cc.hao2.blog.modle.MediaModle;
import cc.hao2.blog.service.PlayMusicService;
import cc.hao2.blog.util.NumberUtil;


public class FragmentRadio extends Fragment {
    private static FragmentRadio f;
    private RecyclerView recyclerView;
    private RadioAdapter adapter;
    private MediaModle modle;
    private String title;

    private PlayMusicService.MusicBinder musicBinder;
    private UpDateMusicInfoReceiver receiver;
    private ServiceConnection conn;

    private SeekBar seekBar;
    private TextView tvCurrentTitle;
    private TextView tvLastTime;
    private CheckBox rbPlay;
    private List<Radio> radios;

    private String currentMusicTitle;
    private String currentMusicUrl;
    private int currentMusicPosition;

    private int current;
    private int total;


    public static FragmentRadio getInstance(String title) {
        FragmentRadio fragment = new FragmentRadio();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        f = fragment;
        return fragment;
    }

    public static FragmentRadio getInstance() {
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        initView(view);
        intData();

        bindMusicService();
        registerComponent();

        setListeners();
        return view;
    }

    private void setListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicBinder.seekTo(progress);
                    Log.i("Test", "onProgressChanged: " + progress);
                    tvLastTime.setText(NumberUtil.durationTimeFormat(total-progress-5000)); //显示数据和音乐时长差5秒
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rbPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if ("万码千钧Blog".equals(tvCurrentTitle.getText())){
                        Radio r = radios.get(0);
                        musicBinder.playMusic(r.getUrl());
                        tvCurrentTitle.setText(r.getName());
                    }else {
                        musicBinder.playOrPause();
                    }
            }
        });

        tvCurrentTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(currentMusicPosition);
            }
        });
    }

    private void registerComponent() {
        receiver = new UpDateMusicInfoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_UPDATE_PROGRESS");
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * 获取Adapter中点击到的数据
     *
     * @param title
     * @param url
     * @param position
     */
    public void getClickData(String title, String url, int position) {
        this.currentMusicPosition = position;
        this.currentMusicTitle = title;
        this.currentMusicUrl = url;

        musicBinder.playMusic(currentMusicUrl);
        rbPlay.setChecked(true);
        tvCurrentTitle.setText(currentMusicTitle);
        seekBar.setProgress(0);
        tvLastTime.setText("");
    }

    private void bindMusicService() {
        Intent intent = new Intent(getActivity(), PlayMusicService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicBinder = (PlayMusicService.MusicBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        getActivity().bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }


    private void intData() {
        modle = new MediaModle();
        modle.getRadioList(new MediaModle.RadioCallBack() {
            @Override
            public void onRadioListLoaded(List<Radio> radios) {
                setData(radios);
                showData();
            }
        });

    }

    private void setData(List<Radio> radios) {
        this.radios = radios;
    }

    private void showData() {
        adapter = new RadioAdapter(getActivity(), radios);
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        tvCurrentTitle = (TextView) view.findViewById(R.id.title);
        tvLastTime = (TextView) view.findViewById(R.id.lastTime);
        rbPlay = (CheckBox) view.findViewById(R.id.rbPlay);

        seekBar.setProgress(0);
        tvCurrentTitle.setText("万码千钧Blog");
    }

    @Override
    public void onDestroy() {
        musicBinder.stop();
        getActivity().unbindService(conn);
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }


    private class UpDateMusicInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("ACTION_UPDATE_PROGRESS".equals(action)) {
                current = intent.getIntExtra("current", 0)+5000; //显示数据和音乐时长差5秒
                total = intent.getIntExtra("total", 0);
                seekBar.setMax(total);
                seekBar.setProgress(current);
                tvLastTime.setText(NumberUtil.durationTimeFormat(total - current));
                if (tvLastTime.getText().equals("00:00")){
                    rbPlay.setChecked(false);
                }
            }
        }
    }
}
