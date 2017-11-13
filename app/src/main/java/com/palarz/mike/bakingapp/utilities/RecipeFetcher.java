package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.palarz.mike.bakingapp.model.Ingredient;
import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import timber.log.Timber;

/**
 * Created by mpala on 10/4/2017.
 */

public class RecipeFetcher {

    private static final String TAG = RecipeFetcher.class.getSimpleName();
    private static final String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_INGREDIENTS = "ingredients";
    private static final String JSON_KEY_INGREDIENT_QUANTITY = "quantity";
    private static final String JSON_KEY_INGREDIENT_MEASURE = "measure";
    private static final String JSON_KEY_INGREDIENT_DESCRIPTION = "ingredient";
    private static final String JSON_KEY_STEPS = "steps";
    private static final String JSON_KEY_STEP_ID = "id";
    private static final String JSON_KEY_STEP_DESCRIPTION_SHORT = "shortDescription";
    private static final String JSON_KEY_STEP_DESCRIPTION_LONG = "description";
    private static final String JSON_KEY_STEP_VIDEO_URL = "videoURL";
    private static final String JSON_KEY_STEP_THUMBNAIL = "thumbnailURL";
    private static final String JSON_KEY_SERVINGS = "servings";
    private static final String JSON_KEY_IMAGE = "image";


    public static URL buildURL(){
        //A Uri is first built according to TheMovieDB API's documentation
        Uri builtUri = Uri.parse(RECIPE_BASE_URL);
        URL url = null;

        try{
            //The Uri is then converted into a URL if possible
            url = new URL(builtUri.toString());
            Timber.d("The resulting built URL: " + url.toString());
        }
        catch (MalformedURLException mue){
            Log.e(TAG, "Encountered a MalformedURLException when building the URL");
            mue.printStackTrace();
        }

        return url;
    }

    public static String getHTTPResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            String httpResponse = null;
            if(scanner.hasNext()) {
                httpResponse = scanner.next();
                Timber.d("Full HTTP response: \n" + httpResponse + "\n");
                return httpResponse;
            }
            return httpResponse;
        }
        finally {
            connection.disconnect();
        }
    }

    private void parseRecipes(String httpResponse, List recipesList) throws JSONException {
        JSONArray jsonResults = new JSONArray(httpResponse);

        for (int i = 0; i < jsonResults.length(); i++){
            JSONObject jsonRecipe = jsonResults.getJSONObject(i);
            int recipeID = jsonRecipe.getInt(JSON_KEY_ID);
            String recipeName = jsonRecipe.getString(JSON_KEY_NAME);

            JSONArray jsonIngredients = jsonRecipe.getJSONArray(JSON_KEY_INGREDIENTS);
            Ingredient[] ingredients = new Ingredient[jsonIngredients.length()];

            for (int j = 0; j < jsonIngredients.length(); j++){
                JSONObject jsonCurrentIngredient = jsonIngredients.getJSONObject(j);
                double quantity = jsonCurrentIngredient.getDouble(JSON_KEY_INGREDIENT_QUANTITY);
                String measure = jsonCurrentIngredient.getString(JSON_KEY_INGREDIENT_MEASURE);
                String description = jsonCurrentIngredient.getString(JSON_KEY_INGREDIENT_DESCRIPTION);
                Ingredient currentIngredient = new Ingredient(description, measure, quantity);
                ingredients[j] = currentIngredient;
            }

            JSONArray jsonSteps = jsonRecipe.getJSONArray(JSON_KEY_STEPS);
            Step[] steps = new Step[jsonSteps.length()];

            for (int k = 0; k < jsonSteps.length(); k++){
                JSONObject jsonCurrentStep = jsonSteps.getJSONObject(k);
                int ID = jsonCurrentStep.getInt(JSON_KEY_STEP_ID);
                String shortDescription = jsonCurrentStep.getString(JSON_KEY_STEP_DESCRIPTION_SHORT);
                String longDescription = jsonCurrentStep.getString(JSON_KEY_STEP_DESCRIPTION_LONG);
                String videoURL = jsonCurrentStep.getString(JSON_KEY_STEP_VIDEO_URL);
                String thumbnail = jsonCurrentStep.getString(JSON_KEY_STEP_THUMBNAIL);
                Step currentStep = new Step(ID, shortDescription, longDescription, videoURL,
                        thumbnail);
                steps[k] = currentStep;
            }

            int servings = jsonRecipe.getInt(JSON_KEY_SERVINGS);
            String image = jsonRecipe.getString(JSON_KEY_IMAGE);
            Recipe currentRecipe = new Recipe(recipeID, recipeName, ingredients, steps, servings, image);


            /*
             We only want to add the Recipe to our Bakery if it hasn't been added before. Therefore,
             we ensure this by checking within this if statement.
              */
            if (Bakery.get().isNewRecipe(currentRecipe)){
                Timber.d("Current recipe added: \n" + currentRecipe.toString());
                recipesList.add(currentRecipe);
            }
        }
    }

    public List<Recipe> fetchRecipes(){
        List<Recipe> recipes = Bakery.get().getRecipes();
        try {
            String httpResponse = getHTTPResponse(buildURL());
            parseRecipes(httpResponse, recipes);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        catch(JSONException je){
            je.printStackTrace();
        }
        return recipes;
    }

}
