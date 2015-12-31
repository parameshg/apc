package com.paramg.android.trydemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatPreferenceActivity
{
    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener()
    {
        @Override
        public boolean onPreferenceChange(Preference settings, Object value)
        {
            String text = value.toString();

            if (settings instanceof ListPreference)
            {
                ListPreference list = (ListPreference) settings;

                int index = list.findIndexOfValue(text);

                settings.setSummary(index >= 0 ? list.getEntries()[index] : null);
            }
            else
            {
                settings.setSummary(text);
            }

            return true;
        }
    };

    private static void bindSummary(Preference settings)
    {
        settings.setOnPreferenceChangeListener(listener);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(settings.getContext());

        Object o = settings instanceof CheckBoxPreference ? sp.getBoolean(settings.getKey(), false) : sp.getString(settings.getKey(), "");

        listener.onPreferenceChange(settings, o);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        addPreferencesFromResource(R.xml.pref_general);

        bindSummary(findPreference(getString(R.string.pref_domain_key)));
        bindSummary(findPreference(getString(R.string.pref_https_key)));
        bindSummary(findPreference(getString(R.string.pref_browser_key)));
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            if (!super.onMenuItemSelected(featureId, item))
            {
                NavUtils.navigateUpFromSameTask(this);
            }

            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
