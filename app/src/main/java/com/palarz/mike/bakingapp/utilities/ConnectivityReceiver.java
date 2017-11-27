/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
Primary purpose: This receiver is used to check the network connection upon initial startup of the
app. As of now, it is only used within RecipeSelection since this is the only activity that requires
this check.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityListener mCallback;

    // A callback that will be used by activities that needed to check network connectivity
    public interface ConnectivityListener{
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public ConnectivityReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (mCallback != null){
            mCallback.onNetworkConnectionChanged(isConnected);
        }
    }

    /*
    A helper method to manually check if a network connection has been established. This can be
    useful in some cases.
     */
    public static boolean isConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) BakingApplication.getInstance().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
