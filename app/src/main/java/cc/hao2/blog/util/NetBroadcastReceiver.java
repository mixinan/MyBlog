package cc.hao2.blog.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cc.hao2.blog.App;
import cc.hao2.blog.entity.Blog;
import cc.hao2.blog.ui.MainActivity;


/**
 * Created by mixinan on 2016/6/4.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    private TextView tvEmpty;
    private TextView tvNoNet;
    private Activity a;

    public NetBroadcastReceiver(Activity activity, TextView tvEmpty, TextView tvNoNet) {
        this.tvEmpty = tvEmpty;
        this.tvNoNet = tvNoNet;
        this.a = activity;
    }

    public NetBroadcastReceiver(Activity activity, TextView tvNoNet) {
        this.tvNoNet = tvNoNet;
        this.a = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isAvailable()) {
            tvNoNet.setText("没有网络，请检查网络连接");
            tvNoNet.setVisibility(View.VISIBLE);

        } else {
            List<Blog> data = App.getApp().getBlogs();
            if (a instanceof MainActivity) {      //如果是首页
                if (data == null) {   //如果数据为空，加载数据
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("正在加载");
                    MainActivity activity = (MainActivity) a;
                    activity.initData(1);
                }
            }
            tvNoNet.setVisibility(View.GONE);
        }
    }
}
