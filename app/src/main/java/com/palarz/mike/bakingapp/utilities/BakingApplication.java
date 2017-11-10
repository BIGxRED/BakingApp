package com.palarz.mike.bakingapp.utilities;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by mpala on 11/9/2017.
 */

/* This class was created so that the Timber.DebugTree was created the moment the app was started.
* This ensured that we'd only be using one instance of the DebugTree and there wouldn't be
* multiple lines of the same activity within LogCat. In other words, this class was only created for
* debugging purposes.
*/
public class BakingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree(){

            // We are overriding this method so that we can add the line number to the debug tag.
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });
    }
}
