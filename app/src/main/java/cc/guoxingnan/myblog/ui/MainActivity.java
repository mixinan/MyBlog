package cc.guoxingnan.myblog.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;

import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.adapter.BlogListAdapter;
import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.module.BlogModule;
import cc.guoxingnan.myblog.view.SpaceItemDecoration;
import cc.guoxingnan.myblog.view.UpRefreshRecyclerView;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, UpRefreshRecyclerView.UpRefreshListener {
    private SwipeRefreshLayout refreshLayout;
    private UpRefreshRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BlogListAdapter adapter;
    private BlogModule module;
    private ArrayList<Blog> blogs;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //第一页数据
        initData(1);
        setListener();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        recyclerView = (UpRefreshRecyclerView) findViewById(R.id.lv);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_red_light);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SpaceItemDecoration decoration = new SpaceItemDecoration(13);
        recyclerView.addItemDecoration(decoration);
    }


    /**
     * 创建BlogModule对象，开启工作线程获取数据，发送过来并显示
     * 返回值为当前上下文对象，用它在Module层回调getDataFromModule（）方法
     */
    private void initData(int currentPage) {
        module = new BlogModule(this, currentPage);
    }


    /**
     * 设置监听事件
     */
    private void setListener() {
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setUpRefreshListener(this);
    }


    /**
     * 下拉刷新的操作
     */
    @Override
    public void onRefresh() {

        if (blogs == null) {
            Toast.makeText(this, "没网，再好的内容也出不来...", Toast.LENGTH_SHORT).show();
        } else {
            String adTitle = blogs.get(1).getTitle();

            if (!"广告".equals(adTitle) && !"关于".equals(adTitle)) {
                adapter.addData(1, new Blog("广告", "2016-05-29", "广告位,待续", "http://guoxingnan.cc/ads/"));
            } else if ("广告".equals(adTitle)) {
                adapter.addData(1, new Blog("关于", "2016-05-29", "这是一个介绍页面", "http://guoxingnan.cc/about_app/"));
                adapter.removeData(2);
            } else if ("关于".equals(adTitle)) {
                adapter.removeData(1);
            }
        }

        refreshLayout.setRefreshing(false);
    }


    /**
     * 在Module层回调，为确保返回值不为null
     * 得到Module返回的数据，并显示
     * @param data
     */
    public void getDataFromModule(ArrayList<Blog> data) {
        //如果是首页，初始化数据
        //如果不是首页，blogs.addAll(blogs)
        if (currentPage == 1) {
            blogs = data;
            adapter = new BlogListAdapter(MainActivity.this, blogs);
            recyclerView.setAdapter(adapter);
        } else {
            blogs.addAll(data);
            adapter.notifyDataSetChanged();
        }

//        Log.i("Test", "activity - getDataFromModule: data.size--" + blogs.size());

        //更新刷新后的界面布局及动画
        recyclerView.onRefreshFinish();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onUpRefresh() {
        if ("去南京路上".equals(blogs.get(blogs.size() - 1).getTitle())) {
            Toast.makeText(this, "数据已经加载完毕", Toast.LENGTH_SHORT).show();
            return;
        }
        refreshLayout.setRefreshing(true);
        currentPage++;
        initData(currentPage);
    }
}
