// Jonathan Torres
// MDF3 - C201708
// ConfigActivity.java

package com.example.john.torresjonathan_ce03;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ConfigActivity extends AppCompatActivity {

    //Value to determine what Widget id was used
    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //get data for the intent of the widget and start the fragment if available
        Intent starter = getIntent();
        mWidgetId =  starter.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if(mWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
            return;
        }

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction().replace(R.id.ConfigFragment_Frame,
                    ConfigFragment.newInstance()).commit();
        }


    }


    //Menu item settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            AppWidgetManager mgr = AppWidgetManager.getInstance(this);

            //Update the Widget when the Screen closes
            WidgetUtil.singleUpdate(this, mgr, mWidgetId, true);


            //Send the result to the system
            Intent result =  new Intent();
            result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
            setResult(RESULT_OK, result);

            finish();
        }

        return true;
    }
}
