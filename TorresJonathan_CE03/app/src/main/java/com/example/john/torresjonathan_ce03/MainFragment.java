// Jonathan Torres
// MDF3 - C201708
// MainFragment.java

package com.example.john.torresjonathan_ce03;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    //Static String for the broadcaster's Action
    public static final String UPDATE_DATA = "com.example.john.torresjonathan_ce03_UPDATE_DATA";

    //Declare the broadcasters that Updates the UI
    private UpdateUI updateUI;


    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forecast_fragment, container, false);
    }

    //Start the Service intent to bring the forecast data
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = new Intent(getActivity(), ForecastService.class);
        getActivity().startService(intent);
    }

    //on Pause, unregister broadcaster

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(updateUI);
    }

    //on Resume, Set the broadcaster and register it
    @Override
    public void onResume() {
        super.onResume();

        updateUI = new UpdateUI();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_DATA);
        getActivity().registerReceiver(updateUI, intentFilter);

    }

    //Broadcast to update the UI with information
    public class UpdateUI extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent != null){

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                String locationSelection = preferences.
                        getString("com.example.john.torresjonathan_ce03.PREF_LOCATION",
                                "NotALocation");

                TextView location_TV = (TextView)getActivity().findViewById(R.id.location_textView);

                switch (locationSelection) {
                    case "Orlando":
                        location_TV.setText(R.string.orlando_fl);
                        break;
                    case "Arecibo":
                        location_TV.setText(R.string.arecibo_pr);
                        break;
                    default:
                        location_TV.setText(R.string.san_francisco_ca);
                        break;
                }

                ArrayList<ForecastClass> allForecastData;

                //Get the data from the service
                //noinspection unchecked
                allForecastData = (ArrayList<ForecastClass>)intent.getSerializableExtra("allForecastData");



                //Declare and set each View
                TextView weekday1_Today_TV = (TextView)getActivity().findViewById(R.id.Date1_Today_TextView);
                TextView weekday1_Tomorrow_TV = (TextView)getActivity().findViewById(R.id.Date1_Tomorrow_TextView);
                TextView weekday1_Next_TV = (TextView)getActivity().findViewById(R.id.Date1_Last_TextView);

                TextView high1_Today_TV = (TextView)getActivity().findViewById(R.id.Date1_Today_Hi_TimeView);
                TextView high1_Tomorrow_TV = (TextView)getActivity().findViewById(R.id.Date1_Tomorrow_Hi_TimeView);
                TextView high1_Late_TV = (TextView)getActivity().findViewById(R.id.Date1_Last_Hi_TimeView);

                TextView low1_Today_TV  = (TextView)getActivity().findViewById(R.id.Date1_Today_Low_TimeView);
                TextView low1_Tomorrow_TV  = (TextView)getActivity().findViewById(R.id.Date1_Tomorrow_Low_TimeView);
                TextView low1_Late_TV  = (TextView)getActivity().findViewById(R.id.Date1_Last_Low_TimeView);

                ImageView today1_Icon = (ImageView)getActivity().findViewById(R.id.Date1_Today_Icon);
                ImageView tomorrow1_Icon = (ImageView)getActivity().findViewById(R.id.Date1_Tomorrow_Icon);
                ImageView last1_Icon = (ImageView)getActivity().findViewById(R.id.Date1_Last_Icon);

                //Set data to each view respectively
                weekday1_Today_TV.setText(allForecastData.get(0).getWeekday());
                high1_Today_TV.setText(getString(R.string.High_String) + "" + allForecastData.get(0).getHigh());
                low1_Today_TV.setText(getString(R.string.Low_String) + "" + allForecastData.get(0).getLow());
                today1_Icon.setImageBitmap(loadIcon(allForecastData.get(0).getIconBytes()));
                weekday1_Tomorrow_TV.setText(allForecastData.get(1).getWeekday());
                high1_Tomorrow_TV.setText(getString(R.string.High_String) + "" + allForecastData.get(1).getHigh());
                low1_Tomorrow_TV.setText(getString(R.string.Low_String) + "" + allForecastData.get(1).getLow());
                tomorrow1_Icon.setImageBitmap(loadIcon(allForecastData.get(1).getIconBytes()));
                weekday1_Next_TV.setText(allForecastData.get(2).getWeekday());
                high1_Late_TV.setText(getString(R.string.High_String) + "" + allForecastData.get(2).getHigh());
                low1_Late_TV.setText(getString(R.string.Low_String) + "" + allForecastData.get(2).getLow());
                last1_Icon.setImageBitmap(loadIcon(allForecastData.get(2).getIconBytes()));


            }

        }

        //Decode the bitmap from an Byte[]
        private Bitmap loadIcon(byte[] iconData){

            return BitmapFactory.decodeByteArray(iconData, 0, iconData.length);

        }
     }
}

