/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import timber.log.Timber;

/* This class was created so that the Timber.DebugTree is created the moment the app is started.
* This ensures that we're only using one instance of the DebugTree and there wouldn't be
* multiple lines of the same activity within LogCat.
*
* It was also created as a helper for the ConnectivityReceiver, where setConnectivityListener()
* ensures that only one activity at a time is set as the callback for the receiver.
*
* This is because the ConnectivityListener instance in the receiver is static and there can only
* ever be one instance of it. By calling setConnectivityListener() within onResume of any
* activity, we are resetting ConnectivityReceiver's mCallback to a new class that has
* implemented the interface.
*/
public class BakingApplication extends Application {

    private static BakingApplication mInstance;

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

        mInstance = this;

        /*
        Within Android N and greater, the CONNECTIVITY_CHANGE broadcast is no longer received. In
        order to get around this, we must programatically define our intent filter.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerReceiver(new ConnectivityReceiver(),
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public static synchronized BakingApplication getInstance(){
        return mInstance;
    }

    /*
    A helper method which sets the ConnectivityListener of the receiver to a new implementation.
     */
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityListener listener){
        ConnectivityReceiver.mCallback = listener;
    }

}
