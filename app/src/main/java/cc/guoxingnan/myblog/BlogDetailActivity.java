package cc.guoxingnan.myblog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class BlogDetailActivity extends AppCompatActivity {
    public static final String TAG = "Test";

    private TextView tvTitle;
    private TextView tvContent;
    private Button btNewer;
    private Button btOlder;

    private String url;
    private int position;
    private String title;
    private String text;
    private String newerTitle;
    private String olderTitle;

    BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        url = getIntent().getExtras().getString("url", "unknow");
        position = getIntent().getExtras().getInt("position",-1);

        Log.i("Test", "onCreate: url--" + url);
        Log.i("Test", "onCreate: position--" + position);
        new InnerThread().start();

        initView();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                title = bundle.getString("title");
                text = bundle.getString("text");
                newerTitle = bundle.getString("newerTitle");
                olderTitle = bundle.getString("olderTitle");
                Log.i(TAG, "onReceive: "+ title + "\n" +text);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("GetData");
        registerReceiver(receiver, filter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(title);
                tvContent.setText(text);
                btNewer.setText(newerTitle);
                btOlder.setText(olderTitle);
            }
        },1288);


    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        btNewer = (Button) findViewById(R.id.bt_newer);
        btOlder = (Button) findViewById(R.id.bt_older);
    }


    private class InnerThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Document doc = Jsoup.connect(url).get();
//			System.out.println(doc);

                /**-------标题-------*/
                final String title = doc.getElementsByTag("title").first().text();
//			System.out.println(title);

                /**-------正文-------*/
                final String text = doc.getElementsByClass("post-content").first().text();
//			System.out.println(text);


                /**-------上一篇，下一篇(标题，链接)-------*/
                String olderTitle = doc.select("a.post-nav-older").first().attr("title");
                olderTitle = olderTitle.replaceAll("Previous post", "上一篇");
                String olderPath = doc.select("a.post-nav-older").first().attr("href");

                String newerTitle = "";
                String newerPath = "";
                if(position == 0){
                    newerTitle = "没有了";
                }else {
                    newerTitle = doc.select("a.post-nav-newer").first().attr("title");
                    newerTitle = newerTitle.replaceAll("Next post", "下一篇");
                    newerPath = doc.select("a.post-nav-newer").first().attr("href");
                }
//			System.out.println(newerTitle);
//			System.out.println(newerTitle);
//			System.out.println(olderTitle);
//			System.out.println(newerPath);
//			System.out.println(olderPath);

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tvTitle.setText(title);
//                        tvContent.setText(text);
//                    }
//                });

                Intent intent = new Intent();
                intent.setAction("GetData");
                intent.putExtra("title", title);
                intent.putExtra("text", text);
                intent.putExtra("newerTitle", newerTitle);
                intent.putExtra("newerPath", newerPath);
                intent.putExtra("olderTitle", olderTitle);
                intent.putExtra("olderPath", olderPath);
                sendBroadcast(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
