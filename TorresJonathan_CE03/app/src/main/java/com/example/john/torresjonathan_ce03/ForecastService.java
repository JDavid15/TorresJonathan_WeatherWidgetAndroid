// Jonathan Torres
// MDF3 - C201708
// ForecastService.java

package com.example.john.torresjonathan_ce03;

import android.app.IntentService;
import android.content.Context;
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
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ForecastService extends IntentService {

    private final ArrayList<ForecastClass> allForecastData = new ArrayList<>();

    public ForecastService(){
        super("ForecastService");

    }

    //OnHandle method to Download all Location data and send them to a Broadcaster to Update the UI
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //If there is no Internet Access, Stop the Service and Inform the user
        if(!checkForConnection()){
            stopSelf();
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String locationSelection = preferences.
                getString("com.example.john.torresjonathan_ce03.PREF_LOCATION",
                        "NotALocation");

        String[] urls = getResources().getStringArray(R.array.forecast_Urls);

        switch (locationSelection) {
            case "Orlando":
                downloadData(urls[0]);
                break;
            case "Arecibo":
                downloadData(urls[1]);
                break;
            default:
                downloadData(urls[2]);
                break;
        }


        Intent update = new Intent();
        update.setAction(MainFragment.UPDATE_DATA);
        update.putExtra("allForecastData", allForecastData);
        sendBroadcast(update);

    }

    //Get the network data from a url
    private String getNetworkData (String weatherUrl){

        try{

            URL url = new URL(weatherUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.connect();

            InputStream is = connection.getInputStream();

            String data = IOUtils.toString(is);

            is.close();

            connection.disconnect();

            return data;

        }catch (Exception  e){
            e.printStackTrace();

            return null;
        }
    }

    //Download data and store it in a ArrayList
    private void downloadData(String url){


        String data = getNetworkData(url);

        try{

            JSONObject outerJson = new JSONObject(data);
            JSONObject innerJSONData = outerJson.getJSONObject("forecast");
            JSONObject simpleForecast = innerJSONData.getJSONObject("simpleforecast");
            JSONArray forecast = simpleForecast.getJSONArray("forecastday");

            for (int i = 0; i < forecast.length() - 1; i++) {

                JSONObject dateObject = forecast.getJSONObject(i);
                JSONObject dateJSON = dateObject.getJSONObject("date");
                String weekdayString = dateJSON.getString("weekday");
                JSONObject highJSON = dateObject.getJSONObject("high");
                String highString = highJSON.getString("fahrenheit");
                JSONObject lowJSON = dateObject.getJSONObject("low");
                String lowString = lowJSON.getString("fahrenheit");
                String iconUrl = dateObject.getString("icon_url");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = downloadIcon(iconUrl);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                allForecastData.add(new ForecastClass(weekdayString, highString, lowString, byteArray));
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //Download a Bitmap image
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
