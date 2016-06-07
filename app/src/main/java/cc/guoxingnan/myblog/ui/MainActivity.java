package cc.guoxingnan.myblog.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cc.guoxingnan.myblog.App;
import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.adapter.BlogListAdapter;
import cc.guoxingnan.myblog.entity.Blog;
import cc.guoxingnan.myblog.module.BlogModule;
import cc.guoxingnan.myblog.util.NetBroadcastReceiver;
import cc.guoxingnan.myblog.util.NetUtil;
import cc.guoxingnan.myblog.util.ToastUtil;
import cc.guoxingnan.myblog.view.SpaceItemDecoration;
import cc.guoxingnan.myblog.view.UpRefreshRecyclerView;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, UpRefreshRecyclerView.UpRefreshListener {
    private App app;
    private TextView tvEmpty;
    private TextView tvNoNet;
    private SwipeRefreshLayout refreshLayout;
    private UpRefreshRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BlogListAdapter adapter;
    private BlogModule module;
    private List<Blog> blogs;
    private int currentPage = 1;

    private NetBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (App) getApplication();
        initView();
        setListener();

        registerReceiver();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //数据为空时的布局
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
        tvNoNet = (TextView) findViewById(R.id.tvNoNet);

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
     * 接收到“有网”的广播以后
     * 创建BlogModule对象，开启工作线程获取数据，发送过来并显示
     * 返回值为当前上下文对象，用它在Module层回调getDataFromModule（）方法
     */
    public void initData(int currentPage) {
        module = new BlogModule();
        module.getBlogList(new BlogModule.Call_Back() {

            @Override
            public void onBlogsLoaded(List<Blog> blogs) {
                setAdapter(blogs);
            }
        }, currentPage);
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
        String adTitle = blogs.get(1).getTitle();

        if (!"广告".equals(adTitle) && !"关于".equals(adTitle)) {
            adapter.addData(1, new Blog("广告", "2016-05-29", "点击查看广告", "http://guoxingnan.cc/ads/"));
            app.play_ad();
        } else if ("广告".equals(adTitle)) {
            adapter.addData(1, new Blog("关于", "2016-05-29", "查看我的介绍", "http://guoxingnan.cc/about_app/"));
            adapter.removeData(2);
            app.play_ad();
        } else if ("关于".equals(adTitle)) {
            adapter.removeData(1);
            app.play_flush();
        }
        stopRefreshing();
    }


    /**
     * 在Module层回调，为确保返回值不为null
     * 得到Module返回的数据，并显示
     *
     * @param data
     */
    public void setAdapter(List<Blog> data) {
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
        stopRefreshing();
        //隐藏掉空消息提示文字
        tvEmpty.setVisibility(View.GONE);
        tvNoNet.setVisibility(View.GONE);
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onUpRefresh() {
        if (!NetUtil.haveNet(this)) {
            return;
        } else {
            if ("去南京路上".equals(blogs.get(blogs.size() - 1).getTitle())) {
                ToastUtil.showToast(this, "数据已经加载完毕");
                return;
            }
            refreshing();
            currentPage++;
            initData(currentPage);
        }
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

    /**
     * 显示刷新动画
     */
    public void refreshing() {
        refreshLayout.setRefreshing(true);
    }

    /**
     * 取消刷新动画
     */
    public void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }


    /*
    以下为广播相关代码
     */

    private void registerReceiver() {
        receiver = new NetBroadcastReceiver(this, tvEmpty, tvNoNet);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);
    }

    private void unregisterReceiver() {
        this.unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}
