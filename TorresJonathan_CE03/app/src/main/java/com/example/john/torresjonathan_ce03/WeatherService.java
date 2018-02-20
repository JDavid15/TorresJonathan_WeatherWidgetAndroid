// Jonathan Torres
// MDF3 - C201708
// WeatherService.java

package com.example.john.torresjonathan_ce03;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherService extends IntentService {

    private static final String FILE_NAME = "location.dat";
    private static  boolean ifSingleUpdate = false;
    private int intentWidgetId;

    public WeatherService(){
        super("WeatherService");

    }

    //onHandle download the data from the location's current weather
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //If there is no Internet Access, Stop the Service and Inform the user
        if(!checkForConnection()){
            stopSelf();
            return;
        }

        //If the service purpose is to update a singular Widget
        if(intent != null){

            ifSingleUpdate = true;
            intentWidgetId = intent.getIntExtra("SINGLE", 0);
        }

        //Download and save all Weather Data from the selected location
        String data = getNetworkData();

        try {
            JSONObject outerJson = new JSONObject(data);
            JSONObject innerJSONData = outerJson.getJSONObject("current_observation");

            String string_Temp = innerJSONData.getString("temperature_string");
            String currentConditions = innerJSONData.getString("weather");
            String imageUrl = innerJSONData.getString("icon_url");
            String string_Time = innerJSONData.getString("local_time_rfc822");

            Bitmap savedBitmap = downloadIcon(imageUrl);


            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File imagePath = new File(directory,"locationIcon.jpg");

            FileOutputStream saveImage = new FileOutputStream(imagePath);
            assert savedBitmap != null;
            savedBitmap.compress(Bitmap.CompressFormat.PNG, 100, saveImage);
            saveImage.close();

            LocationData locationData = new LocationData
                    (string_Temp, currentConditions, string_Time, directory.getAbsolutePath());

            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(locationData);
            oos.close();


            //Set the manager to Update the Widget's UI based on preferences
            AppWidgetManager mgr = AppWidgetManager.getInstance(this);

            ComponentName componentName = new ComponentName(this, WeatherWidgetProvider.class);

            int[] widgetsIds = mgr.getAppWidgetIds(componentName);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            String themeString = preferences.
                    getString("com.example.john.torresjonathan_ce03.PREF_THEME",
                            "Light");

            if(themeString.equals("Dark")){
                mgr.notifyAppWidgetViewDataChanged(widgetsIds ,R.layout.light_widget_layout);
            }else{
                mgr.notifyAppWidgetViewDataChanged(widgetsIds ,R.layout.dark_widget_layout);
            }


            if(ifSingleUpdate){
                WidgetUtil.singleUpdate(this, mgr, intentWidgetId, false);
            }else{
                WidgetUtil.multiUpdate(this, mgr, widgetsIds);
            }

            stopSelf();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //Function to get the Network Data
    private String getNetworkData (){

        try{

            //Set the data based on the Preference selected
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            String[] urls = getResources().getStringArray(R.array.location_Urls);

            String locationUrl;

            String locationSelection = preferences.
                    getString("com.example.john.torresjonathan_ce03.PREF_LOCATION",
                            "NotALocation");

            switch (locationSelection) {
                case "Orlando":
                    locationUrl = urls[0];
                    break;
                case "Arecibo":
                    locationUrl = urls[1];
                    break;
                default:
                    locationUrl = urls[2];
                    break;
            }

            URL url = new URL(locationUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.connect();

            InputStream is = connection.getInputStream();

            String data = IOUtils.toString(is);

            is.close();

            connection.disconnect();

            return data;

        }catch (Exception e){
            e.printStackTrace();

            return "noUpdate";
        }
    }

    //Download a bitmap
    private Bitmap downloadIcon(String src){

        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkForConnection(){
        //If Statements to check if there is connectivity on the phone
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mgr != null) {

            NetworkInfo info = mgr.getActiveNetworkInfo();

            if(info != null){

                boolean isConnected = info.isConnected();

                if (isConnected){

                    return true;

                }else{
                    Toast.makeText(this, R.string.noInternet_Toast, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, R.string.noInternet_Toast, Toast.LENGTH_LONG).show();
            }
        }
        return false;

    }
}
