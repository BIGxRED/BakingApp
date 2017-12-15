/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import com.palarz.mike.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/*
Primary purpose: This is an interface for the Retrofit library. Normally, we'd include multiple
GET and POST HTTP calls for a more advanced application. However, we only have one HTTP response
to take care of for now.
 */
public interface RecipesClient {

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    /*
    A method which obtains all of the recipes from the BASE_URL.
     */
    @GET("baking.json")
    Call<List<Recipe>> getAllRecipes();
}
