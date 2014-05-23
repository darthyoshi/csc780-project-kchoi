/**
 * @file PreferencesFragment.java
 * @author Kay Choi
 */
package com.example.untouchable.fragments;

import com.example.untouchable.*;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.*;

public class PreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        final Activity parent = getActivity();
        
        findPreference("resetScores").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                
                builder.setMessage(parent.getResources().getString(R.string.resetQuestion))
                    .setNegativeButton(parent.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle(parent.getResources().getString(R.string.resetScores))
                    .setPositiveButton(parent.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ScoreManager.resetScores(parent);
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
        //updates description to show current difficulty
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
