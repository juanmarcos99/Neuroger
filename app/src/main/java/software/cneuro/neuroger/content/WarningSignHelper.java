package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;

/**
 * Created by klaudia on 22/09/2016.
 */

public class WarningSignHelper {
    public static String[] getWarningSigns(Context context) {
        return context.getResources().getStringArray(R.array.warning_signs);
    }

    public static String getWarningSignText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.warning_signs);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String[] getWarningHelp(Context context) {
        return context.getResources().getStringArray(R.array.warning_help);
    }

    public static String getWarningHelpText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.warning_help);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }
}
