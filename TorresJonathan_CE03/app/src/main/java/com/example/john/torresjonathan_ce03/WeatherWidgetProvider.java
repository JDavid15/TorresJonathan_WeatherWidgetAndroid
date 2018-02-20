// Jonathan Torres
// MDF3 - C201708
// WeatherWidgetProvider.java

package com.example.john.torresjonathan_ce03;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;




public class WeatherWidgetProvider extends AppWidgetProvider {


    //Update the widget when needed
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        WidgetUtil.multiUpdate(context, appWidgetManager, appWidgetIds);
    }

    //On received is called when update is needed
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        context.startService(new Intent(context, WeatherService.class));


    }

}
