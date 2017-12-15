/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import okhttp3.OkHttpClient;

/*
Primary purpose: This class is used in order to ensure that the same OkHttpClient is used within
both the RecipeSelection class and the OkHttp Idling Resource (used for testing).

This technique was taken from the following GitHub repository:
https://github.com/chiuki/espresso-samples/tree/master/idling-resource-okhttp
 */
public abstract class OkHttpClientProvider {

    private static OkHttpClient sClient = null;

    public static OkHttpClient getOkHttpClient() {
        if (sClient == null) {
            sClient = new OkHttpClient();
        }
        return sClient;
    }

}
