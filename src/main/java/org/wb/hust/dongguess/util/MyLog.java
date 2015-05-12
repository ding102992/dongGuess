package org.wb.hust.dongguess.util;

import android.util.Log;

/**
 * Created by Administrator on 2015/1/6.
 */
public class MyLog {
    private static final boolean STATE_SWITCH = true;

    public static void i(String tag,String msg){
        if(STATE_SWITCH) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag,String msg){
        if(STATE_SWITCH){
            Log.d(tag,msg);
        }
    }

    public static void e(String tag,String msg){
        if(STATE_SWITCH){
            Log.e(tag,msg);
        }
    }

}
