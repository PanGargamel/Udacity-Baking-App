package pl.piotrskiba.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import pl.piotrskiba.bakingapp.models.Ingredient;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.utils.StringUtils;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        // check if there's already selected recipe
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(String.valueOf(appWidgetId), null);
        if(json != null){
            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(json, Recipe.class);
            views.setTextViewText(R.id.widget_title, recipe.getName());
            views.setTextViewText(R.id.widget_ingredients, new StringUtils().getIngredientsList(context, recipe));
        }
        else{
            // get default string values
            CharSequence defaultWidgetTitle = context.getString(R.string.widget_default_title);
            CharSequence defaultWidgetIngredients = context.getString(R.string.widget_default_content);

            // set default values
            views.setTextViewText(R.id.widget_title, defaultWidgetTitle);
            views.setTextViewText(R.id.widget_ingredients, defaultWidgetIngredients);
        }

        // Create an Intent to launch recipe selection when clicked
        Intent intent  = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.ACTION_SELECT_RECIPE, true);
        intent.putExtra(MainActivity.KEY_WIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // set on click listener
        views.setOnClickPendingIntent(R.id.widget_linear_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

