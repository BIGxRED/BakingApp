/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.model.Ingredient;
import com.palarz.mike.bakingapp.utilities.Bakery;

import timber.log.Timber;

public class GroceriesListRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new GroceriesListRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class GroceriesListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    public static final String BUNDLE_KEY_RECIPE_ID = "recipe_id";

    Context mContext;
    int mRecipeID;
    Ingredient[] mIngredients;

    public GroceriesListRemoteViewsFactory(Context newContext, Intent recipeIDIntent){
        this.mContext = newContext;
        this.mRecipeID = recipeIDIntent.getIntExtra(BUNDLE_KEY_RECIPE_ID, -1);
    }

    @Override
    public void onCreate() {
        // If we have a valid recipe ID value, then we'll obtain the array of ingredients
        if (mRecipeID != -1) {
            this.mIngredients = Bakery.get().getRecipe(mRecipeID).getIngredients();
        }

        // Otherwise, we will log the error
        else {
            Timber.e("Could not extract the recipe with the provided ID: " + mRecipeID);
        }
    }

    @Override
    public int getCount() {
        if (mIngredients == null){
            return 0;
        }

        return mIngredients.length;
    }

    @Override
    public void onDataSetChanged() {
        // If we don't have any ingredients to show, then we'll simply exit the method
        if (mIngredients == null || mRecipeID == -1) {
            return;
        }

        // Otherwise, we'll get the updated ingredients
        mIngredients = Bakery.get().getRecipe(mRecipeID).getIngredients();
    }

    @Override
    public RemoteViews getViewAt(int index) {
        Ingredient currentIngredient = mIngredients[index];
        if (currentIngredient == null){
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.app_widget_ingredient_list_item);
        remoteViews.setTextViewText(R.id.app_widget_ingredient_list_item_description,
                currentIngredient.toString());

        return remoteViews;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDestroy() {
        mIngredients = null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
