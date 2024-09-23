package com.example.apptransport.Util;

import android.app.Activity;

/**
 * Common Display utility class
 *
 * @author Luis Hernández
 * @version 1.0
 */
public class DisplayUtil {

    private static final String STATUS_BAR_IDENTIFIER = "status_bar_height";
    private static final String STATUS_BAR_PROPERTY_TYPE = "dimen";
    private static final String STATUS_BAR_DEFAULT_PACKAGE = "android";

    /**
     * Get android status bar height in pixels
     *
     * @param context Activity running screen
     * @return int height in pixels
     */
    public static int getStatusBarHeight(Activity context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier(STATUS_BAR_IDENTIFIER, STATUS_BAR_PROPERTY_TYPE, STATUS_BAR_DEFAULT_PACKAGE);
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
