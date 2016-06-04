package cc.guoxingnan.myblog.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mixinan on 2016/6/4.
 */
public class NetUtil {

    public static boolean haveNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isAvailable()) {
            return false;
        }
        return true;
    }
}
