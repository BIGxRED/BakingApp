package com.palarz.mike.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.palarz.mike.bakingapp.R;
import com.palarz.mike.bakingapp.activities.RecipeSelection;

/**
 * Created by mpala on 11/15/2017.
 */

public class GroceryListAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; i++){
            int appWidgetID = appWidgetIds[i];

            // Creating an intent to launch RecipeSelection
            Intent intent = new Intent(context, RecipeSelection.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Getting a reference to the layout of the widget and setting an on-click listener to
            // the dummy TextView
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setOnClickPendingIntent(R.id.app_widget_test, pendingIntent);

            // Finally, we inform the widget manager to update the current widget
            appWidgetManager.updateAppWidget(appWidgetID, views);

        }
    }
}
