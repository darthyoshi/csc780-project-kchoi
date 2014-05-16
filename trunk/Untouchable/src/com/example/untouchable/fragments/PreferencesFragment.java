/**
 * @file PreferencesFragment.java
 * @author Kay Choi
 */
package com.example.untouchable.fragments;

import com.example.untouchable.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.*;

public class PreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        findPreference("resetScores").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                
                builder.setMessage("Really reset scores? This cannot be undone.")
                    .setNegativeButton(getActivity().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Reset Scores")
                    .setPositiveButton(getActivity().getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ScoreManager.resetScores(getActivity());
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false);
                
                AlertDialog dialog = builder.create();
                dialog.show();
                
                
                return true;
            }
        });
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if(key.equals("difficulty")) {
            Preference pref = findPreference(key);
            pref.setSummary(((ListPreference)pref).getEntry());
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
