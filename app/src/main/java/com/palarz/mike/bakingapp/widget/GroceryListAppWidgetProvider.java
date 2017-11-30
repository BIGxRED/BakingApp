/*
The following code is the property and sole work of Mike Palarz, a student at Udacity.
 */

package com.palarz.mike.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.palarz.mike.bakingapp.R;

import java.util.Random;

import timber.log.Timber;

public class GroceryListAppWidgetProvider extends AppWidgetProvider {

    public static final String GROCERY_LIST_ACTION = "com.palarz.mike.bakingapp.GROCERY_LIST_ACTION";
//    public static final String GROCERY_LIST_CONTENTS = "com.palarz.mike.bakingapp.GROCERY_LIST_CONTENTS";
    int mRandomNumber;


    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        // First we check if we've received the broadcast we're interested in
        if (intent.getAction().equals(GROCERY_LIST_ACTION)){
            Timber.d("Within onReceive() and the action is a match");
            // Next, we pull the widget ID, String of groceries, and recipe ID all from the Intent
            int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
//            String groceries = intent.getStringExtra(GROCERY_LIST_CONTENTS);
            int recipeID = intent.getIntExtra(GroceriesListRemoteViewsFactory.BUNDLE_KEY_RECIPE_ID, -1);
            Timber.d("Value of recipe ID in onReceive(): " + recipeID);

            RemoteViews updatedView = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            Intent groceriesListIntent = new Intent(context, GroceriesListRemoteViewsService.class);
            groceriesListIntent.putExtra(GroceriesListRemoteViewsFactory.BUNDLE_KEY_RECIPE_ID, recipeID);
            mRandomNumber = new Random().nextInt();
            groceriesListIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetID + mRandomNumber), null));

            updatedView.setRemoteAdapter(R.id.app_widget_list_view_groceries, groceriesListIntent);
            updatedView.setEmptyView(R.id.app_widget_list_view_groceries, R.id.app_widget_empty_view_groceries);

            // Finally, we update the individual widget with the updated RemoteViews.
            Timber.d("updateAppWidget() is called...");

//            manager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.app_widget_list_view_groceries);
            manager.updateAppWidget(appWidgetID, updatedView);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; i++){

            // We set up an Intent which starts the RecipesListRemoteViewsService which will
            // provide the views for the ListView within our widget
            Timber.d("onUpdate():\n");
            Intent remoteViewsServiceIntent = new Intent(context, RecipesListRemoteViewsService.class);

            // We create an instance of the RemoteViews object
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            /*
            Now we begin setting up the RemoteViews object to use an adapter. This adapter connects
            to a RemoteViewsService through the specified intent which is where the data is
            populated.
             */
            Timber.d("Setting remote adapter for recipes");
            remoteViews.setRemoteAdapter(R.id.app_widget_list_view_recipes, remoteViewsServiceIntent);

            /*
            The empty view is displayed when the ListView has no data to display. It should be in
            the same layout XML as the RemoteViews object created above.
             */
            Timber.d("Setting empty view for recipes");
            remoteViews.setEmptyView(R.id.app_widget_list_view_recipes, R.id.app_widget_empty_view_recipes);


//            // Setting up the grocery list ListView as well....
//            Intent groceriesListIntent = new Intent(context, GroceriesListRemoteViewsService.class);
//            groceriesListIntent.putExtra(GroceriesListRemoteViewsFactory.BUNDLE_KEY_RECIPE_ID, -1);
//
//            Timber.d("Setting remote adapter for groceries");
//            remoteViews.setRemoteAdapter(R.id.app_widget_list_view_groceries, groceriesListIntent);
//            Timber.d("Setting empty view for groceries");
//            remoteViews.setEmptyView(R.id.app_widget_list_view_groceries, R.id.app_widget_empty_view_groceries);

            /*
            We'd also like the functionality of allowing for each item within the ListView to
            update the groceries list. In order to do so, we create a PendingIntent
            template for the entire ListView. Then, a fill-in Intent is created for each item
            within the ListView in the GroceryListViewsService.
             */
            Intent intentTemplate = new Intent(context, GroceryListAppWidgetProvider.class);

            // We set an action which will be handled within onReceive()
            intentTemplate.setAction(GROCERY_LIST_ACTION);
            // We also stored the widget ID so that we know which widget needs to be updated
            intentTemplate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(
                    context, 0, intentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.app_widget_list_view_recipes, pendingIntentTemplate);

            // Finally, we tell the AppWidgetManager to update the current widget within the array
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }
}
