package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;
import software.cneuro.neuroger.constant.Constant;

/**
 * Created by klaudia on 22/09/2016.
 */

public class MMSEHelper {
    public static String getLanguageOrderIn3Text(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.order_in_3);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String[] getOrientationTemporal(Context context) {
        return context.getResources().getStringArray(R.array.temporal_orientation);
    }

    public static String[] getOrientationSpatial(Context context) {
        return context.getResources().getStringArray(R.array.spatial_orientation);
    }

    public static String[] getRetentionInfo(Context context) {
        return context.getResources().getStringArray(R.array.retention_and_info);
    }

    public static String[] getMathAttention(Context context) {
        return context.getResources().getStringArray(R.array.math_and_attention);
    }

    public static String[] getReminder(Context context) {
        return context.getResources().getStringArray(R.array.reminder);
    }

    public static String[] getLanguage(Context context) {
        return context.getResources().getStringArray(R.array.language);
    }

    public static String[] getOrderIn3(Context context) {
        return context.getResources().getStringArray(R.array.order_in_3);
    }

    public static String[] getDesign(Context context) {
        return context.getResources().getStringArray(R.array.design);
    }

    public static String getMiniMentalStateHelpText(Context context, int helpID) {
        switch (helpID) {
            case Constant.HELP_ORIENTATION_TEMPORAL:
                return context.getString(R.string.orientation_temporal_help);
            case Constant.HELP_ORIENTATION_SPATIAL:
                return context.getString(R.string.orientation_spatial_help);
            case Constant.HELP_RETENTION:
                return context.getString(R.string.retention_help);
            case Constant.HELP_MATH_ATTENTION:
                return context.getString(R.string.math_help);
            case Constant.HELP_REMINDER:
                return context.getString(R.string.reminder_help);
            case Constant.HELP_DESIGN:
                return context.getString(R.string.design_help);

            case Constant.HELP_LANGUAGE_1:
                return context.getString(R.string.language_1_help);
            case Constant.HELP_LANGUAGE_2:
                return context.getString(R.string.language_2_help);
            case Constant.HELP_LANGUAGE_3:
                return context.getString(R.string.language_3_help);
            case Constant.HELP_LANGUAGE_4:
                return context.getString(R.string.language_4_help);
            case Constant.HELP_LANGUAGE_5:
                return context.getString(R.string.language_5_help);
            case Constant.HELP_ORDER:
                return context.getString(R.string.order_help);
        }

        return "";
    }

    public static String getOrientationTemporalText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.temporal_orientation);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getOrientationSpatialText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.spatial_orientation);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getRetentionInfoText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.retention_and_info);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getMathAttentionText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.math_and_attention);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getReminderText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.reminder);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getLanguageText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.language);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getDesignText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.design);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getNoAnswerText(Context context) {
        return context.getResources().getString(R.string.patient_no_respond);
    }
}
