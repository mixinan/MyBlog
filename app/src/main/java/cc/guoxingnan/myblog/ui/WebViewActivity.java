package cc.guoxingnan.myblog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cc.guoxingnan.myblog.R;
import cc.guoxingnan.myblog.util.MenuUtil;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    private String url, title;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                MenuUtil.shareText(WebViewActivity.this, "《" + title + "》" + url + "（from:万码千钧Blog）", "分享到:");
                break;
            case R.id.action_open_in_browser:
                MenuUtil.openInChrome(this,url);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

}