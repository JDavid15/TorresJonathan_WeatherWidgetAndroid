// Jonathan Torres
// MDF3 - C201708
// ConfigFragment.java

package com.example.john.torresjonathan_ce03;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public class ConfigFragment extends PreferenceFragment {

    public static ConfigFragment newInstance() {

        Bundle args = new Bundle();

        ConfigFragment fragment = new ConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Set the preference screen
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_config);
    }

}
