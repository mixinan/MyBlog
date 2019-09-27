package cc.hao2.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mixinan on 2016/6/14.
 */
public class NumberUtil {
    public static String durationTimeFormat(long haomiao){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.CHINA);
        if (haomiao <= 0){
            return "00:00";
        }
        return sdf.format(new Date(haomiao));
    }

    public static String publishTimeFormat(long haomiao){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(new Date(haomiao));
    }
}
