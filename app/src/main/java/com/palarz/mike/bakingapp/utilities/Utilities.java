/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.utilities;

import android.content.Context;
import android.content.res.Configuration;
import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.model.Recipe;

import java.util.Random;

/*
This is a helper class that includes methods that are used throughout the app.
 */
public class Utilities {

    /*
    An array of drawable IDs that are used only when the recipe image could not be downloaded. This
    array is used within getImageResource().
     */
    public static final int[] RECIPE_IMAGES = {
            R.drawable.brownies,
            R.drawable.cheesecake,
            R.drawable.nutella,
            R.drawable.yellow_cake,
            R.drawable.random_recipe1,
            R.drawable.random_recipe2,
            R.drawable.random_recipe3,
            R.drawable.random_recipe4,
            R.drawable.random_recipe5,
            R.drawable.random_recipe6,
            R.drawable.random_recipe7,
            R.drawable.random_recipe8
    };

    /*
    A set of static ints that are only used if the recipe is one that we already know of (is
    contained in the current form of the JSON response).
     */
    public static final int INDEX_BROWNIES = 0;
    public static final int INDEX_CHEESECAKE = 1;
    public static final int INDEX_NUTELLA = 2;
    public static final int INDEX_YELLOW_CAKE = 3;
    public static final int INDEX_RANDOM_BEGINNING = 4;
    public static final int IMAGE_RESOURCES_LENGTH = RECIPE_IMAGES.length;

    /*
    An array of drawable IDs that are used only when the step image could not be downloaded. This
    array is used within getStepImageResource().
     */
    public static final int [] STEP_IMAGES = {
            R.drawable.step1,
            R.drawable.step2,
            R.drawable.step3,
            R.drawable.step4,
            R.drawable.step5,
            R.drawable.step6,
            R.drawable.step7,
            R.drawable.step8,
            R.drawable.step9,
            R.drawable.step10,
            R.drawable.step11,
            R.drawable.step12,
            R.drawable.step13,
            R.drawable.step14,
            R.drawable.step15,
            R.drawable.step16,
            R.drawable.step17,
            R.drawable.step18,
            R.drawable.step19,
            R.drawable.step20,
            R.drawable.step21,
            R.drawable.step22,
            R.drawable.step23,
            R.drawable.step24
    };

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

    /*
    The purpose of this function is to generate a random drawable ID in case the Recipe object
    does not have anything specified for the JSON "image" key. This function has taken into account
    that some of the Recipes are known based on the current state of the JSON data. Therefore,
    a drawable ID is only created for the random drawables that have been provided.
     */
    public static int getRandomImageResource(){
        /*
        This version of nextInt() allows us to place a range for the int value that is returned -
        the argument for nextInt() is exclusive of the random number that will be generated.
        Therefore, nextInt() will generate a random int within 0 inclusive and the argument value
        exclusive. We than add INDEX_RANDOM_BEGINNING to randomIndex to make sure that the returned
        value is always at least INDEX_RANDOM_BEGINNING.
         */
        int randomIndex = new Random().nextInt(IMAGE_RESOURCES_LENGTH - INDEX_RANDOM_BEGINNING) + INDEX_RANDOM_BEGINNING;

        return RECIPE_IMAGES[randomIndex];
    }

    /*
    This is a helper method for obtaining a drawable ID when the recipe does not contain a URL for
    the image. If the recipe is one of the four that is currently within the JSON response, then
    we use an image that suits that particular recipe.
     */
    public static int getImageResource(Recipe recipe){
        String recipeName = recipe.getName();

        switch (recipeName){
            case Recipe.RECIPE_BROWNIES:
                return RECIPE_IMAGES[INDEX_BROWNIES];
            case Recipe.RECIPE_CHEESECAKE:
                return RECIPE_IMAGES[INDEX_CHEESECAKE];
            case Recipe.RECIPE_NUTELLA_PIE:
                return RECIPE_IMAGES[INDEX_NUTELLA];
            case Recipe.RECIPE_YELLOW_CAKE:
                return RECIPE_IMAGES[INDEX_YELLOW_CAKE];
            default:
                return getRandomImageResource();
        }
    }

    /*
    A helper method for obtaining a random drawable ID for a step if the step does not contain a
    suitable URL for the image.
     */
    public static int getRandomStepImageResource(){
        int randomIndex = new Random().nextInt(STEP_IMAGES.length);

        return STEP_IMAGES[randomIndex];
    }

}
