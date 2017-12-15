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

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        // First we check if we've received the broadcast we're interested in
        if (intent.getAction().equals(GROCERY_LIST_ACTION)){

            // Next, we pull the widget ID and recipe ID from the Intent
            int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // Here we set the default value as -1. If we refer to how
            // GroceriesListRemoteViewsService was designed, this is also our error case value. If
            // mRecipeID within the service = -1, then we handle the situation accordingly
            // throughout the service.
            int recipeID = intent.getIntExtra(GroceriesListRemoteViewsFactory.BUNDLE_KEY_RECIPE_ID, -1);

            // We create an updated RemoteViews instance so that we can update the list of
            // groceries when a different recipe is clicked
            RemoteViews updatedView = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            // An Intent for the GroceriesListRemoteViewsService is created
            Intent groceriesListIntent = new Intent(context, GroceriesListRemoteViewsService.class);

            // We attach the recipe ID to the intent so that the groceries list service knows
            // which recipe's ingredients should be shown
            groceriesListIntent.putExtra(GroceriesListRemoteViewsFactory.BUNDLE_KEY_RECIPE_ID, recipeID);

            /*
            This part is critical: the RemoteViewsService will cache the factory that is created
            for each widget ID. In order for a new RemoteViews to be recognized by the service,
            it needs to believe that a new app widget ID is being used. Therefore, we add a
            random integer to the app widget ID so that the  service believes it needs to create
            a new factory.
             */
            int randomNumber = new Random().nextInt();
            groceriesListIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetID + randomNumber), null));

            // We setup the adapter for the groceries list as well as the empty view
            updatedView.setRemoteAdapter(R.id.app_widget_list_view_groceries, groceriesListIntent);
            updatedView.setEmptyView(R.id.app_widget_list_view_groceries, R.id.app_widget_empty_view_groceries);

            /*
            Finally, we update the individual widget with the updated RemoteViews. We ensure that
            the correct widget is still being updated by using the original widget ID, not the
            one with the random int added to it.
             */
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
            Intent remoteViewsServiceIntent = new Intent(context, RecipesListRemoteViewsService.class);

            // We create an instance of the RemoteViews object
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            /*
            Now we begin setting up the RemoteViews object to use an adapter. This adapter connects
            to a RemoteViewsService through the specified intent which is where the data is
            populated.
             */
            remoteViews.setRemoteAdapter(R.id.app_widget_list_view_recipes, remoteViewsServiceIntent);

            /*
            The empty view is displayed when the ListView has no data to display. It should be in
            the same layout XML as the RemoteViews object created above.
             */
            remoteViews.setEmptyView(R.id.app_widget_list_view_recipes, R.id.app_widget_empty_view_recipes);

            /*
            We'd also like the functionality of allowing for each item within the ListView to
            update the groceries list. In order to do so, we create a PendingIntent
            template for the entire ListView. Then, a fill-in Intent is created for each item
            within the ListView in the RecipesListRemoteViewsService.
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
