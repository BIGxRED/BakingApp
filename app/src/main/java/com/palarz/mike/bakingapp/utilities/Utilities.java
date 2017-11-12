package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/*
This is a helper class that includes methods that are used throughout the app.
 */
public class Utilities {

    // Value that I'm using for the smallest width of a tablet
    public static final int TABLET_SMALLEST_WIDTH = 600;

    /*
    This is a helper method used to to determine if the display is a tablet. This method is useful
    in both RecipeSelection (to change the number of columns) and StepWatcher (whether to hide the
    system UI or not when in landscape orientation).
     */
    public static boolean isTablet(Context context){
        // First we create a DisplayMetrics object
        DisplayMetrics metrics = new DisplayMetrics();

        // We then obtain a reference to the current WindowManager and reset metrics to the current
        // display metrics
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        // We then obtain the absolute number of pixels for both the width and height; however,
        // these are the absolute values, which are dependent on both the size and type of screen.
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        // In order to convert the height and width into pixels, we can use the scale factor from
        // our DisplayMetrics object.
        float densityScaleFactor = metrics.density;

        // Therefore, we convert the width and height into dips...
        float widthDips = widthPixels / densityScaleFactor;
        float heightDips = heightPixels / densityScaleFactor;

        // ...and determine which of the dimensions is smaller.
        float smallestWidth = Math.min(widthDips, heightDips);

        // Finally, we compare the smaller dimension to 600dp
        return smallestWidth >= TABLET_SMALLEST_WIDTH;
    }

}
