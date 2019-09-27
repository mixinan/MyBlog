package cc.hao2.blog.util;

import android.util.Log;

public class LogUtil {

        // 开发时设置为6，都输出
        // 发布时设置为0，不输出
        private static int LOG_LEVEL = 6;

        private static int VERBOSE = 1;
        private static int DEBUG = 2;
        private static int INFO = 3;
        private static int WARN = 4;
        private static int ERROR = 5;

        public static void v(String tag, String msg){
            if (LOG_LEVEL > VERBOSE){
                Log.v(tag, msg);
            }
        }

        public static void d(String tag, String msg){
            if (LOG_LEVEL > DEBUG){
                Log.v(tag, msg);
            }
        }

        public static void i(String tag, String msg){
            if (LOG_LEVEL > INFO){
                Log.v(tag, msg);
            }
        }

        public static void w(String tag, String msg){
            if (LOG_LEVEL > WARN){
                Log.v(tag, msg);
            }
        }

        public static void e(String tag, String msg){
            if (LOG_LEVEL > ERROR){
                Log.v(tag, msg);
            }
        }

}
