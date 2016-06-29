package cc.guoxingnan.myblog.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.adapter.VideoAdapter;
import cc.guoxingnan.myblog.entity.Video;
import cc.guoxingnan.myblog.modle.MediaModle;

public class FragmentVideo extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private MediaModle modle;
    private String title;

    public static FragmentVideo getInstance(String title) {
        FragmentVideo fragment = new FragmentVideo();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        initView(view);
        intData();
        return view;
    }

    private void intData() {
        modle = new MediaModle();
        modle.getVideoList(new MediaModle.VideoCallBack() {
            @Override
            public void onVideoListLoaded(List<Video> videos) {
                setAdapter(videos);
            }
        });

    }

    private void setAdapter(List<Video> videos) {
        adapter = new VideoAdapter(getActivity(), videos);
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
