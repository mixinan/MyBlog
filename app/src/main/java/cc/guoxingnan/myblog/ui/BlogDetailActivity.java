package cc.guoxingnan.myblog.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.module.BlogDetailModule;

public class BlogDetailActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {
    private BlogDetailModule module;

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

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        url = getIntent().getExtras().getString("url", "unknow");
        position = getIntent().getExtras().getInt("position", -1);

        initView();
        initData(url, position);
        setListener();
    }

    private void setListener() {
        btNewer.setOnClickListener(this);
        btOlder.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initData(String url, int position) {
        refreshLayout.setRefreshing(true);
        module = new BlogDetailModule(this, url, position);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
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

        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_newer:
                if (btNewer.getText().equals("首页")) {
                    finish();
//                    Toast.makeText(this, "已经是最新一篇了", Toast.LENGTH_SHORT).show();
                    break;
                }
                initData(newerPath, position -- );
                scrollToTop();
                break;


            case R.id.bt_older:
                if (btOlder.getText().equals("没有了")) {
                    Toast.makeText(this, "已经是最早一篇了", Toast.LENGTH_SHORT).show();
                    break;
                }
                initData(olderPath, position + 1);
                scrollToTop();
                break;

            case R.id.tv_title:
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
        if (btOlder.getText().equals("没有了")) {
            Toast.makeText(this, "已经是最早一篇了", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
            return;
        }
        initData(olderPath, position + 1);
        scrollToTop();
    }
}
