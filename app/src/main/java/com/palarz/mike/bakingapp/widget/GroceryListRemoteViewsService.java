/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.model.Recipe;
import com.palarz.mike.bakingapp.utilities.Bakery;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;

import java.util.List;

public class GroceryListRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GroceryListRemoteViewsFactory(this.getApplicationContext());
    }
}

class GroceryListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    Context mContext;
    List<Recipe> mRecipes;

    public GroceryListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        mRecipes = Bakery.get().getRecipes();
    }

    @Override
    public int getCount() {
        if (mRecipes == null){
            return 0;
        }
        return mRecipes.size();
    }

    @Override
    public void onDataSetChanged() {
        mRecipes = Bakery.get().getRecipes();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        // First, we obtain a reference to the current Recipe
        Recipe currentRecipe = mRecipes.get(i);

        // If the return Recipe is empty, then we will return null
        if (currentRecipe == null){
            return null;
        }

        // Otherwise, we will setup the RemoteView for the currentRecipe...
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.app_widget_list_item);

        //...and set the recipe name for the current list item.
        remoteViews.setTextViewText(R.id.app_widget_list_item_recipe_name, currentRecipe.getName());

        /*
        In order for the grocery list TextView to be updated each time a recipe is clicked, we
        must create a fill-in Intent. To do so, we first create a Bundle which contains whatever
        data is needed for this Intent.
        */
        Bundle extras = new Bundle();
        extras.putInt(RecipeAdapter.EXTRA_RECIPE_ID, currentRecipe.getID());
        extras.putString(GroceryListAppWidgetProvider.GROCERY_LIST_CONTENTS, currentRecipe.printIngredients());

        /*
        We then create an Intent that contains the extras and set the fill-in Intent to the
        individual list item of the ListView.
        */
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.app_widget_list_item_container, fillInIntent);

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
        mRecipes.clear();
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
