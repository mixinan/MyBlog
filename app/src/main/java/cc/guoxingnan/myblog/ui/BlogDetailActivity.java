package cc.guoxingnan.myblog.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import cc.guoxingnan.myblog.App;
import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.module.BlogDetailModule;
import cc.guoxingnan.myblog.util.NetBroadcastReceiver;
import cc.guoxingnan.myblog.util.NetUtil;
import cc.guoxingnan.myblog.util.ToastUtil;

public class BlogDetailActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {
    private App app;

    private BlogDetailModule module;

    private TextView tvNoNet;
    private TextView tvTitle;
    private TextView tvContent;
    private Button btNewer;
    private Button btOlder;
    private ScrollView scrollView;
    private SwipeRefreshLayout refreshLayout;

    private String url;
    private int position;

    private String olderPath;
    private String newerPath;

    private NetBroadcastReceiver receiver;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        app = (App) getApplication();

        url = getIntent().getExtras().getString("url", "unknow");
        position = getIntent().getExtras().getInt("position", -1);

        initView();
        initData(url, position);
        setListener();

        registerReceiver();
    }

    private void setListener() {
        btNewer.setOnClickListener(this);
        btOlder.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initData(String url, int position) {
        if (!NetUtil.haveNet(this)) {
            stopRefreshing();
        } else {
            module = new BlogDetailModule(this, url, position);
        }
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvNoNet = (TextView) findViewById(R.id.tvNoNet);
        tvContent = (TextView) findViewById(R.id.tv_content);
        btNewer = (Button) findViewById(R.id.bt_newer);
        btOlder = (Button) findViewById(R.id.bt_older);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_red_light);
    }

    public void showData(String title, String text, String newerTitle, String newerPath, String olderTitle, String olderPath) {
        this.newerPath = newerPath;
        this.olderPath = olderPath;

        tvTitle.setText(title);
        tvContent.setText(text);
        btNewer.setText(newerTitle);
        btOlder.setText(olderTitle);
        app.play_flush();
        stopRefreshing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_newer:
                if (btNewer.getText().equals("首页")) {
                    finish();
                    break;
                }
                refreshing();
                initData(newerPath, position--);
                scrollToTop();
                break;


            case R.id.bt_older:
                if (btOlder.getText().equals("没有了")) {
                    ToastUtil.showToast(this, "已经是最后一篇了，年轻人，不要太贪啊！");
                    break;
                }
                refreshing();
                initData(olderPath, position + 1);
                scrollToTop();
                break;

            case R.id.tv_title:
                app.play_top();
                scrollToTop();
                break;
        }
    }

    /**
     * scrollView滚动到顶部
     */
    private void scrollToTop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }


    @Override
    public void onRefresh() {
        if ("去南京路上".equals(tvTitle.getText().toString().trim())) {
            ToastUtil.showToast(this, "年轻人，没数据了");
            stopRefreshing();
            return;
        }
        initData(olderPath, position + 1);
        scrollToTop();
    }



    /*
    以下为广播相关代码
     */

    private void registerReceiver() {
        receiver = new NetBroadcastReceiver(this, tvNoNet);
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


    /**
     * 显示刷新动画
     */
    public void refreshing(){
        refreshLayout.setRefreshing(true);
    }

    /**
     * 取消刷新动画
     */
    public void stopRefreshing(){
        refreshLayout.setRefreshing(false);
    }
}
