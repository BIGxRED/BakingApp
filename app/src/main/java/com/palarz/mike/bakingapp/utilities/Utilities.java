package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.content.res.Configuration;
import com.palarz.mike.bakingapp.R;

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

        return context.getResources().getBoolean(R.bool.isTablet);
    }

    /*
    A helper method which determines if the device is in landscape orientation.
     */
    public static boolean isLandscape(Context context){
        return context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
    }

}
