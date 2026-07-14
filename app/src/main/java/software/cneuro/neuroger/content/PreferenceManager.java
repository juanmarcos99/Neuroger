package software.cneuro.neuroger.content;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by klaudia on 20/02/2018.
 * Helper class to do operations on shared preferences.
 */

public class PreferenceManager {
    private static final String PREFERENCES_FILE = "neuroger_preference";
    private final static String KEY_HOSPITAL_SELECTED = "hospital_selected";
    private final static String KEY_COUNTRY_SELECTED = "country_selected";
    private final static String KEY_PROVINCE_SELECTED = "province_selected";
    private final static String KEY_MUNICIPALITY_SELECTED = "municipality_selected";

    private SharedPreferences mPreferences;

    public PreferenceManager(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public int getPreferenceHospital() {
        return getFromPreferences(KEY_HOSPITAL_SELECTED);
    }

    public int getPreferenceCountry() {
        return getFromPreferences(KEY_COUNTRY_SELECTED);
    }

    public int getPreferenceProvince() {
        return getFromPreferences(KEY_PROVINCE_SELECTED);
    }

    public int getPreferenceMunicipality() {
        return getFromPreferences(KEY_MUNICIPALITY_SELECTED);
    }

    public void setPreferenceHospital(int position) {
        writeToPreferences(KEY_HOSPITAL_SELECTED, position);
    }

    public void setPreferenceCountry(int position) {
        writeToPreferences(KEY_COUNTRY_SELECTED, position);
    }

    public void setPreferenceProvince(int position) {
        writeToPreferences(KEY_PROVINCE_SELECTED, position);
    }

    public void setPreferenceMunicipality(int position) {
        writeToPreferences(KEY_MUNICIPALITY_SELECTED, position);
    }

    /**
     * Write a value to a user preferences file.
     *
     * @param key A string for the key that will be used to retrieve the value in the future.
     * @param value An int representing the value to be inserted.
     */
    private void writeToPreferences(String key, int value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**     *
     * Get a value from a user preferences file.
     *
     * @param key A key that will be used to retrieve the value from the preference file.
     * @return An int representing the value retrieved from the preferences file.
     */
    private int getFromPreferences(String key) {
        return mPreferences.getInt(key, 0);
    }
}
