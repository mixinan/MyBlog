package cc.guoxingnan.myblog.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mixinan on 2016/5/31.
 */
public class ToastUtil {
    private static Toast toast;

    private ToastUtil(){
    }

    public static void showToast(Context context, String string){
        if (toast == null){
            toast = Toast.makeText(context, string,Toast.LENGTH_SHORT);
        }else{
            toast.setText(string);
        }
        toast.show();
    }
}
