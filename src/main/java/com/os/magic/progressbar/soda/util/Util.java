package com.os.magic.progressbar.soda.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by chan on 15-11-26.
 */
public class Util {

    static public int dip2Px(@NonNull Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    static public int dp2Px(@NonNull Context context,int dp) {
        return dip2Px(context, dp);
    }

    static public float getScreenDensity(@NonNull Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

}
