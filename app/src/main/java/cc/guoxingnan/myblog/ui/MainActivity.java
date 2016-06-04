package cc.guoxingnan.myblog.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cc.guoxingnan.myblog.App;
import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.adapter.BlogListAdapter;
import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.module.BlogModule;
import cc.guoxingnan.myblog.util.NetUtil;
import cc.guoxingnan.myblog.util.ToastUtil;
import cc.guoxingnan.myblog.view.SpaceItemDecoration;
import cc.guoxingnan.myblog.view.UpRefreshRecyclerView;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, UpRefreshRecyclerView.UpRefreshListener {
    private App app;
    private LinearLayout emptyLayout;
    private TextView tvEmpty;
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
        app = (App) getApplication();
        initView();
        //第一页数据
        initData(1);
        setListener();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //数据为空时的布局
        emptyLayout = (LinearLayout) findViewById(R.id.emptyLayout);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);

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
        if (!NetUtil.haveNet(this)) {
            tvEmpty.setText("没有网络\n点击重新加载");
        } else {
            tvEmpty.setText("正在加载数据");
            module = new BlogModule(this, currentPage);
        }
    }


    /**
     * 设置监听事件
     */
    private void setListener() {
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setUpRefreshListener(this);
        tvEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtil.haveNet(MainActivity.this)) {
                    ToastUtil.showLongToast(MainActivity.this, "还是没网啊");

                } else {
                    initData(1);
                }
            }
        });
    }


    /**
     * 下拉刷新的操作
     */
    @Override
    public void onRefresh() {
        String adTitle = blogs.get(1).getTitle();

        if (!"广告".equals(adTitle) && !"关于".equals(adTitle)) {
            adapter.addData(1, new Blog("广告", "2016-05-29", "广告位,待续", "http://guoxingnan.cc/ads/"));
            app.play_ad();
        } else if ("广告".equals(adTitle)) {
            adapter.addData(1, new Blog("关于", "2016-05-29", "这是一个介绍页面", "http://guoxingnan.cc/about_app/"));
            adapter.removeData(2);
            app.play_ad();
        } else if ("关于".equals(adTitle)) {
            adapter.removeData(1);
            app.play_flush();
        }
        refreshLayout.setRefreshing(false);
    }


    /**
     * 在Module层回调，为确保返回值不为null
     * 得到Module返回的数据，并显示
     *
     * @param data
     */
    public void getDataFromModule(ArrayList<Blog> data) {
        //如果是首页，初始化数据
        //如果不是首页，blogs.addAll(blogs)
        if (currentPage == 1) {
            blogs = data;
            adapter = new BlogListAdapter(MainActivity.this, blogs);
            recyclerView.setAdapter(adapter);
            app.play_flush();
        } else {
            blogs.addAll(data);
            adapter.notifyDataSetChanged();
            app.play_flush();
        }

//        Log.i("Test", "activity - getDataFromModule: data.size--" + blogs.size());

        //更新刷新后的界面布局及动画
        recyclerView.onRefreshFinish();
        refreshLayout.setRefreshing(false);
        //隐藏掉空消息提示文字
        emptyLayout.setVisibility(View.GONE);
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onUpRefresh() {
        if ("去南京路上".equals(blogs.get(blogs.size() - 1).getTitle())) {
            ToastUtil.showToast(this, "数据已经加载完毕");
            return;
        }
        refreshLayout.setRefreshing(true);
        currentPage++;
        initData(currentPage);
    }


    /**
     * 再按一次退出
     */
    private long lastBackTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackTime <= 1500) {
            finish();
        }
        lastBackTime = System.currentTimeMillis();
        ToastUtil.showToast(this, "再按一次退出");
    }


    /**
     * 关闭时，Toast瞬间消失
     */
    @Override
    protected void onPause() {
        super.onPause();
        ToastUtil.cancelToast();
    }

}
