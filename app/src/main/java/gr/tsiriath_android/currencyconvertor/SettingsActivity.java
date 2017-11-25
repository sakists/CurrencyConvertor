package gr.tsiriath_android.currencyconvertor;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.Toast;


public class SettingsActivity extends PreferenceActivity
    implements Preference.OnPreferenceChangeListener{

    private SharedPreferences mySharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

       // bindPreferencesSummaryToValue(findPreference(getString(R.string.pref_autoUpdate_key)));
        bindPreferencesSummaryToValue(findPreference(getString(R.string.pref_selectBase_key)));
    }

    private void bindPreferencesSummaryToValue(Preference preference){
        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
//                mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                boolean BCupdFlag = mySharedPreferences.getBoolean(getString(R.string.pref_BaseCurAutoUpdate_key),Boolean.valueOf(getString(R.string.pref_BaseCurAutoUpdate_key)));
//                if (BCupdFlag){
//                    MainActivity.showToast(getApplicationContext(), getString(R.string.Try_to_update), Toast.LENGTH_LONG);
//                    //String pref_baseCur = mySharedPreferences.getString(getString(R.string.pref_selectBase_key),getString(R.string.pref_selectBase_def));
//                    FetchCurrenciesTask task = new FetchCurrenciesTask();
//                    task.execute(this);
//                }
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}

