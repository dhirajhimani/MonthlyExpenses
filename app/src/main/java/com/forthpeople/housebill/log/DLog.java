package com.forthpeople.housebill.log;

import android.util.Log;

/**
 * Created by dhiraj on 16-12-2015.
 */
public class DLog {

    private static boolean debug = true;

    public  static void d(String TAG, String msg) {
        if(debug) {
            Log.d(TAG, msg);
        }
    }

    public  static void e(String TAG, String msg) {
        if(debug) {
            Log.e(TAG, msg);
        }
    }
}
