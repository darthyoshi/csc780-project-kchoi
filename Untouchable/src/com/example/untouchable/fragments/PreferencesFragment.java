/**
 * @file PreferencesFragment.java
 * @author Kay Choi
 */
package com.example.untouchable.fragments;

import com.example.untouchable.*;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
