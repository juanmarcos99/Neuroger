package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;

public class PASHelper {
    public static String getTestTypesText(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.psychofamily_tests);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return "";
        }

        return array[pagePosition];
    }

    public static int getTotalItems(Context context) {
        return context.getResources().getStringArray(R.array.psychofamily_tests).length;
    }

    public static String[] getItemsList(Context context, int pagePosition) {
        String[] result = new String[]{};

        switch (pagePosition) {
            case 0:
                result = getFamilySituationItemsList(context);
                break;
            case 1:
                result = getRelationshipsContactsItemsList(context);
                break;
            case 2:
                result = getSocialNetworkSupportsItemsList(context);
                break;
        }

        return result;
    }

    private static String[] getFamilySituationItemsList(Context context) {
        return context.getResources().getStringArray(R.array.family_situation);
    }

    private static String[] getRelationshipsContactsItemsList(Context context) {
        return context.getResources().getStringArray(R.array.relationships_contacts);
    }

    private static String[] getSocialNetworkSupportsItemsList(Context context) {
        return context.getResources().getStringArray(R.array.social_network_supports);
    }

    public static String getItemsList(Context context, int pagePosition, int answerPosition) {
        String result = "";

        switch (pagePosition) {
            case 0:
                result = getFamilySituationText(context, answerPosition);
                break;
            case 1:
                result = getRelationshipsContactsItemText(context, answerPosition);
                break;
            case 2:
                result = getSocialNetworkSupportsItemText(context, answerPosition);
                break;
        }

        return result;
    }

    private static String getFamilySituationText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.family_situation);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getRelationshipsContactsItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.relationships_contacts);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getSocialNetworkSupportsItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.social_network_supports);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    public static String getHelp(Context context) {
        return context.getString(R.string.psychofamily_help);
    }
}
