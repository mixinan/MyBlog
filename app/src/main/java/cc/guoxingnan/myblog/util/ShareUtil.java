package cc.guoxingnan.myblog.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

public class ShareUtil {

    /**
     * 分享本篇博客
     * @param context
     * @param text
     */
    public static void shareCurrentBlog(Context context, String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "分享本篇博客到："));
    }


    /**
     * 复制到剪贴板
     * @param context
     * @param url
     */
    public static void copyToClipboard(Context context, String url){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("url",url));
        ToastUtil.showToast(context,"已经复制到剪贴板");
    }
}
