package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;

public class FrailHelper {
    public static String getHelp(Context context) {
        return context.getString(R.string.frail_help);
    }

    public static String[] getFatigue(Context context) {
        return context.getResources().getStringArray(R.array.fatigue);
    }

    public static String[] getResistance(Context context) {
        return context.getResources().getStringArray(R.array.resistance);
    }

    public static String[] getWandering(Context context) {
        return context.getResources().getStringArray(R.array.wandering);
    }

    public static String[] getDiseases(Context context) {
        return context.getResources().getStringArray(R.array.diseases);
    }

    public static String getFatigueText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.fatigue);
        if (position < 0 || position >= array.length) {
            return "";
        }
        return array[position];
    }

    public static String getResistanceText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.resistance);
        if (position < 0 || position >= array.length) {
            return "";
        }
        return array[position];
    }

    public static String getWanderingText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.wandering);
        if (position < 0 || position >= array.length) {
            return "";
        }
        return array[position];
    }

    public static String getDiseasesText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.diseases);
        if (position < 0 || position >= array.length) {
            return "";
        }
        return array[position];
    }
}
