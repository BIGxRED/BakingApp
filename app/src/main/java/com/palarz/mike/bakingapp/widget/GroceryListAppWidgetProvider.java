package com.palarz.mike.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.activities.RecipeDetails;
import com.palarz.mike.bakingapp.activities.RecipeSelection;

/**
 * Created by mpala on 11/15/2017.
 */

public class GroceryListAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; i++){

            // We set up an Intent which starts the GroceryListRemoteViewsService which will
            // provide the views for the ListView within our widget
            Intent remoteViewsServiceIntent = new Intent(context, GroceryListRemoteViewsService.class);

            // We add the app widget ID as an extra to the Intent
            remoteViewsServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            // Not so sure of this next line... It was in the guide from Google regarding widgets
            remoteViewsServiceIntent.setData(Uri.parse(remoteViewsServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));

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
            launch the RecipeDetails activity. In order to do so, we create a PendingIntent
            template for the entire ListView. Then, a fill-in Intent is created for each item
            within the ListView in the GroceryListViewsService.
             */
            Intent intentTemplate = new Intent(context, RecipeDetails.class);
            PendingIntent pendingIntentTemplate = PendingIntent.getActivity(
                    context, 0, intentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.app_widget_list_view, pendingIntentTemplate);

            // Finally, we tell the AppWidgetManager to update the current widget within the array
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

        }
    }
}
