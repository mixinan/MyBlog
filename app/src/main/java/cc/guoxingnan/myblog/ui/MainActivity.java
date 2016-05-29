package cc.guoxingnan.myblog.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.adapter.BlogListAdapter;
import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.module.BlogModule;
import cc.guoxingnan.myblog.view.SpaceItemDecoration;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BlogListAdapter adapter;
    private BlogModule module;
    private ArrayList<Blog> blogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setListener();
    }

    private void setListener() {
        refreshLayout.setOnRefreshListener(this);
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.lv);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_red_light);
    }


    private void initData() {
        module = new BlogModule(this);
    }


    /**
     * 在Module层调用，传入获取的数据集合，防止在Activity里数据为空
     *
     * @param blogs
     */
    public void setMyAdapter(ArrayList<Blog> blogs) {
        this.blogs = blogs;

        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new BlogListAdapter(MainActivity.this, blogs);
        recyclerView.setAdapter(adapter);

        SpaceItemDecoration decoration = new SpaceItemDecoration(13);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void onRefresh() {
        String adTitle = blogs.get(1).getTitle();

        if (!"广告".equals(adTitle) && !"关于".equals(adTitle)) {
            adapter.addData(1, new Blog("广告", "2016-05-29", "这是一个广告位", "http://guoxingnan.cc/save-money/"));
        }else if ("广告".equals(adTitle)){
            adapter.addData(1, new Blog("关于", "2016-05-29", "这是一个介绍页面", "http://guoxingnan.cc/about_lizhi_fm/"));
            adapter.removeData(2);
        }else if ("关于".equals(adTitle)){
            adapter.removeData(1);
        }
        refreshLayout.setRefreshing(false);
    }
}
