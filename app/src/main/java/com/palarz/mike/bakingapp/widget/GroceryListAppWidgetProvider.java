/*
The following code is the property and sole work of Mike Palarz, a student at Udacity
 */

package com.palarz.mike.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.activities.RecipeDetails;
import com.palarz.mike.bakingapp.utilities.RecipeAdapter;

public class GroceryListAppWidgetProvider extends AppWidgetProvider {

    public static final String GROCERY_LIST_ACTION = "com.palarz.mike.bakingapp.GROCERY_LIST_ACTION";
    public static final String GROCERY_LIST_CONTENTS = "com.palarz.mike.bakingapp.GROCERY_LIST_CONTENTS";


    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        // First we check if we've received the broadcast we're interested in
        if (intent.getAction().equals(GROCERY_LIST_ACTION)){

            // Next, we pull the widget ID, String of groceries, and recipe ID all from the Intent
            int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String groceries = intent.getStringExtra(GROCERY_LIST_CONTENTS);
            int recipeID = intent.getIntExtra(RecipeAdapter.EXTRA_RECIPE_ID, -1);

            // We create a new RemoteViews instance so that the contents of the groceries list
            // can be updated for the current recipe
            RemoteViews updatedView = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            updatedView.setTextViewText(R.id.app_widget_groceries_list, groceries);

            /*
            The grocery list in the widget only provides a preview of all of the groceries. In order
            for the entire grocery list to be made available, we allow the user to click on the
            grocery list to launch RecipeDetails to see the full list of ingredients. In order to
            offer this feature, we've setup a PendingIntent on the grocery list TextView.
             */
            Intent onClickIntent = new Intent(context, RecipeDetails.class);
            onClickIntent.putExtra(RecipeAdapter.EXTRA_RECIPE_ID, recipeID);

            PendingIntent onClickPendingIntent =
                    PendingIntent.getActivity(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            updatedView.setOnClickPendingIntent(R.id.app_widget_groceries_list, onClickPendingIntent);

            // Finally, we update the individual widget with the updated RemoteViews.
            manager.updateAppWidget(appWidgetID, updatedView);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; i++){

            // We set up an Intent which starts the GroceryListRemoteViewsService which will
            // provide the views for the ListView within our widget
            Intent remoteViewsServiceIntent = new Intent(context, GroceryListRemoteViewsService.class);

            // We create an instance of the RemoteViews object
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            /*
            Now we begin setting up the RemoteViews object to use an adapter. This adapter connects
            to a RemoteViewsService through the specified intent which is where the data is
            populated.
             */
            remoteViews.setRemoteAdapter(R.id.app_widget_list_view, remoteViewsServiceIntent);

            /*
            The empty view is displayed when the ListView has no data to display. It should be in
            the same layout XML as the RemoteViews object created above.
             */
            remoteViews.setEmptyView(R.id.app_widget_list_view, R.id.app_widget_empty_view);

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
            remoteViews.setPendingIntentTemplate(R.id.app_widget_list_view, pendingIntentTemplate);

            // Finally, we tell the AppWidgetManager to update the current widget within the array
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }
}
