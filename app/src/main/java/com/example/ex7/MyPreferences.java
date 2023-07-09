package com.example.ex7;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;
import java.util.prefs.Preferences;

public class MyPreferences extends PreferenceFragmentCompat {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.white));
        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Retrieve the ListPreference
        ListPreference languagePreference = findPreference("selected_language");

        // Set a listener to detect changes in the selected language
        if (languagePreference != null) {
            languagePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String selectedLanguage = (String) newValue;
                    Locale locale = new Locale(selectedLanguage);
                    Locale.setDefault(locale);

                    Configuration configuration = new Configuration();
                    configuration.locale = locale;

                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                    getActivity().recreate(); // Recreate the activity to apply the language changes
                    return true;
                }
            });
        }
    }
}
