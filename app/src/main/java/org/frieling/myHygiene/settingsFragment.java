package org.frieling.myHygiene;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class settingsFragment extends PreferenceFragmentCompat {

    public settingsFragment(){}


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

    }
}
