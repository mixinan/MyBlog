package cc.hao2.blog.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MenuUtil {

    /**
     * 分享文本
     * @param context  上下文对象
     * @param content  要分享的内容
     * @param chooserTitle  选择器的标题
     */
    public static void shareText(Context context, String content, String chooserTitle){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(intent, chooserTitle));
    }


    /**
     * 复制到剪贴板
     * @param context
     * @param url
     */
    public static void copyToClipboard(Context context, String url){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("url",url));
        ToastUtil.showToast(context,"已经复制到剪贴板");
    }


    /**
     * 用浏览器打开
     * @param context
     * @param uri
     */
    public static void openInChrome(Context context, String uri){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }
}
