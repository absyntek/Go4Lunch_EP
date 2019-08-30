package com.EtiennePriou.go4launch.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    public int getLayoutContentViewID() {
        return R.layout.settings_activity;
    }

    @Override
    protected void setupUi() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void withOnCreate() {

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}