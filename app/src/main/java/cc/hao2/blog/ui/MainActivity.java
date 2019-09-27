package cc.hao2.blog.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cc.hao2.blog.App;
import cc.hao2.blog.R;
import cc.hao2.blog.adapter.BlogListAdapter;
import cc.hao2.blog.entity.Blog;
import cc.hao2.blog.modle.BlogIndexPageModle;
import cc.hao2.blog.util.NetBroadcastReceiver;
import cc.hao2.blog.util.NetUtil;
import cc.hao2.blog.util.ToastUtil;
import cc.hao2.blog.view.SpaceItemDecoration;
import cc.hao2.blog.view.UpRefreshRecyclerView;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, UpRefreshRecyclerView.UpRefreshListener {
    private App app;
    private Toolbar toolbar;

    private TextView tvNoNet;
    private TextView tvEmpty;

    private SwipeRefreshLayout refreshLayout;
    private UpRefreshRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BlogListAdapter adapter;
    private BlogIndexPageModle module;
    private BlogIndexPageModle bIndexModule;
    private List<Blog> blogs;
    private int currentPage = 1;

    private NetBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (App) getApplication();
        blogs = app.getBlogs();

        initView();
        setAdapter(blogs);
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("万码千钧Blog");

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
     * 创建 BlogIndexPageModle 对象，开启异步任务获得数据，并回调
     */
    public void initData(int currentPage) {
        module = new BlogIndexPageModle();
        module.getBlogList(new BlogIndexPageModle.Call_Back() {

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
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.play_top();
                if (blogs != null) {
                    recyclerView.smoothScrollToPosition(0);
                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("正在加载");
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
        if (!NetUtil.haveNet(this)){
            ToastUtil.showToast(this,"请先联网");
            stopRefreshing();
            return;
        }
        if (blogs == null) {
            initData(1);
        } else {
            String adTitle = blogs.get(1).getTitle();

            if (!"广告".equals(adTitle) && !"关于".equals(adTitle)) {
                adapter.addData(1, new Blog("广告", "2016-05-29", "点击查看广告", "http://blog.2hao.cc/2016/05/30/ad/"));
                app.play_ad();
            } else if ("广告".equals(adTitle)) {
                adapter.addData(1, new Blog("关于", "2016-05-29", "查看我的介绍", "http://blog.2hao.cc/2016/06/30/about/"));
                adapter.removeData(2);
                app.play_ad();
            } else if ("关于".equals(adTitle)) {
                adapter.removeData(1);
                app.play_flush();
            }
        }
        stopRefreshing();
    }


    /**
     * 显示回调后得到的数据
     * @param data
     */
    public void setAdapter(List<Blog> data) {
        //如果是首页，初始化数据
        //如果不是首页，blogs.addAll(blogs)
        if (currentPage == 1) {
            this.blogs = data;
            adapter = new BlogListAdapter(MainActivity.this, data);
            recyclerView.setAdapter(adapter);
            app.play_flush();
        } else {
            try {
                blogs.addAll(data);
                // 存到app
                app.setData(blogs);
                adapter.notifyDataSetChanged();
                app.play_flush();
            }catch (Exception e){
                e.printStackTrace();
            }

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
            if ("早年发的微博".equals(blogs.get(blogs.size() - 1).getTitle())) {
                ToastUtil.showToast(this, "数据已经加载完毕");
                return;
            }
            refreshing();
            currentPage++;
            initData(currentPage);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.action_video:
                if (NetUtil.haveNet(this)) {
                    startActivity(new Intent(MainActivity.this, MediaActivity.class));
                }else{
                    ToastUtil.showToast(this,"请先联网");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * 再按一次退出
     */
    private long lastBackTime;

    @Override
    public void onBackPressed() {
        if (refreshLayout.isRefreshing()) {
            stopRefreshing();
        } else {
            if (System.currentTimeMillis() - lastBackTime <= 1500) {
                finish();
            }
            lastBackTime = System.currentTimeMillis();
            ToastUtil.showToast(this, "再按一次退出");
        }
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
