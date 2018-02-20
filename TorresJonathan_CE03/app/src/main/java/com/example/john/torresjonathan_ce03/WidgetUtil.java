// Jonathan Torres
// MDF3 - C201708
// WidgetUtil.java

package com.example.john.torresjonathan_ce03;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


 class WidgetUtil {


     //Load data from a saved file in the device
    private static final String FILE_NAME = "location.dat";

    //Update the ui of a single Widget
      static void singleUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Boolean fromActivity){

          if(fromActivity){
              Intent singleUpdateIntent = new Intent(context, WeatherService.class);
              singleUpdateIntent.putExtra("SINGLE", appWidgetId);
              context.startService(singleUpdateIntent);
          }

          //Set preference data
          RemoteViews widgetView;

          SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

          String themeString = preferences.
                  getString("com.example.john.torresjonathan_ce03.PREF_THEME",
                          "Light");

          if(themeString.equals("Dark")){
              widgetView = new RemoteViews(context.getPackageName(), R.layout.dark_widget_layout);
          }else{
              widgetView = new RemoteViews(context.getPackageName(), R.layout.light_widget_layout);
          }


        //Load and update the UI based on the data received
        LocationData locationData;

        try{
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            locationData = (LocationData)ois.readObject();
            ois.close();


            widgetView.setTextViewText(R.id.Status_TextView, locationData.getCondition());
            widgetView.setTextViewText(R.id.Temp_TextView, locationData.getTemp());
            widgetView.setTextViewText(R.id.Time_TextView, locationData.getDataTime());

            Intent perfIntent = new Intent(context, ConfigActivity.class);
            perfIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent prefPending = PendingIntent.getActivity(context, 0, perfIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            widgetView.setOnClickPendingIntent(R.id.Config_Btn ,prefPending);

            Intent activityIntent = new Intent(context, MainActivity.class);
            PendingIntent activityPending = PendingIntent.getActivity(context, 0, activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            widgetView.setOnClickPendingIntent(R.id.weatherImage_ImageView ,activityPending);


            File file = new File(locationData.getImageUrl(), "locationIcon.jpg");
            Bitmap icon = BitmapFactory.decodeStream(new FileInputStream(file));
            widgetView.setImageViewBitmap(R.id.weatherImage_ImageView, icon);

        }catch (Exception e){
            e.printStackTrace();
        }



        //Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetView);
    }

    //The same process as before but for multiple Widgets
    static void multiUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        for (int appWidgetId : appWidgetIds) {

            RemoteViews widgetView;

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            String themeString = preferences.
                    getString("com.example.john.torresjonathan_ce03.PREF_THEME",
                            "Light");

            if(themeString.equals("Dark")){
                widgetView = new RemoteViews(context.getPackageName(), R.layout.dark_widget_layout);
            }else{
                widgetView = new RemoteViews(context.getPackageName(), R.layout.light_widget_layout);
            }

            appWidgetManager.updateAppWidget(appWidgetId, widgetView);
        }

    }


}
