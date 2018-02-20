// Jonathan Torres
// MDF3 - C201708
// MainActivity.java

package com.example.john.torresjonathan_ce03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Simple class that opens a Fragment to the MainFragment to display Forecast Data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.MainActivity_Frame,
                MainFragment.newInstance()).commit();

    }
}
