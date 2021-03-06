package cc.hao2.blog.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import cc.hao2.blog.App;
import cc.hao2.blog.R;
import cc.hao2.blog.modle.BlogDetailModle;
import cc.hao2.blog.util.LogUtil;
import cc.hao2.blog.util.MenuUtil;
import cc.hao2.blog.util.NetBroadcastReceiver;
import cc.hao2.blog.util.NetUtil;
import cc.hao2.blog.util.ToastUtil;

public class BlogDetailActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {
    private App app;
    private Toolbar toolbar;

    private BlogDetailModle module;

    private TextView tvNoNet;
    private TextView tvContent;
    private Button btNewer;
    private Button btOlder;
    private ScrollView scrollView;
    private SwipeRefreshLayout refreshLayout;

    //以下四项，是从首页传过来的数据
    private String url;
    private String title;
    private String text;
    private int position;

    private String olderPath;
    private String newerPath;
    private String currentPath;
    private String currentTitle;

    private NetBroadcastReceiver receiver;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        app = (App) getApplication();

        url = getIntent().getExtras().getString("url", "unknow");
        title = getIntent().getExtras().getString("title", "unknow");
        text = getIntent().getExtras().getString("text", "unknow");
        position = getIntent().getExtras().getInt("position", -1);

        initView();
        initData(url, position);
        setListener();

        registerReceiver();
    }

    private void setListener() {
        btNewer.setOnClickListener(this);
        btOlder.setOnClickListener(this);
        toolbar.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initData(String url, int position) {
        if (!NetUtil.haveNet(this)) {
            stopRefreshing();
        } else {
            module = new BlogDetailModle(this, url, position);
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNoNet = (TextView) findViewById(R.id.tvNoNet);
        tvContent = (TextView) findViewById(R.id.tv_content);
        btNewer = (Button) findViewById(R.id.bt_newer);
        btOlder = (Button) findViewById(R.id.bt_older);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_red_light);

        //先设置为：从首页传来的内容
        getSupportActionBar().setTitle(title);
        tvContent.setText(text);
    }

    public void showData(String title, String text, String newerTitle, String newerPath, String olderTitle, String olderPath, String currentPath) {
        this.newerPath = newerPath;
        this.olderPath = olderPath;
        this.currentPath = currentPath;
        this.currentTitle = title;

        getSupportActionBar().setTitle(title);
        tvContent.setText(Html.fromHtml(text));      //设置为解析网页得到的完整内容
        btNewer.setText(newerTitle);
        btOlder.setText(olderTitle);
        app.play_flush();
        stopRefreshing();
    }

    @Override
    public void onClick(View v) {
        ToastUtil.cancelToast();
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

            case R.id.toolbar:
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
        if ("早年发的微博".equals(getSupportActionBar().getTitle())) {
            ToastUtil.showToast(this, "年轻人，没数据了");
            stopRefreshing();
            return;
        }
        initData(olderPath, position + 1);
        scrollToTop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_share:
                MenuUtil.shareText(this,"《"+currentTitle+ "》" + currentPath,"分享博文到：");
                break;
            case R.id.action_copy_url:
                LogUtil.i("share", "url: "+currentPath);
                MenuUtil.copyToClipboard(this,currentPath);
                break;
            case R.id.action_open_in_browser:
                MenuUtil.openInChrome(this,currentPath);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void refreshing() {
        refreshLayout.setRefreshing(true);
    }

    /**
     * 取消刷新动画
     */
    public void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }
}
