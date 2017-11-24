/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import android.app.Application;

import timber.log.Timber;

/* This class was created so that the Timber.DebugTree is created the moment the app is started.
* This ensures that we're only using one instance of the DebugTree and there wouldn't be
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
